from __future__ import annotations

import gettext
import os.path
from typing import Callable, Protocol
import gi

gi.require_version('Gtk', '3.0')
from gi.repository import Gtk, GLib

PATH_SRC = os.path.dirname(os.path.abspath(__file__)).__str__()


def run(application_id: str, on_activate: Callable) -> None:
    app = Gtk.Application(application_id=application_id)
    app.connect('activate', on_activate)
    app.run(None)


class CheatViewHandler(Protocol):
    def on_built(view: CheatView) -> None: pass


def fill_store(store):
    with open(PATH_SRC + '/comandos.txt') as f:
        for linea in f:
            store.append([linea.strip()])


class CheatView:
    window: Gtk.ApplicationWindow = None

    def __init__(self, idioma):
        self.label = None
        self.handler = None
        self.entry = None
        self.spinner = None
        language = gettext.translation(idioma, localedir='locales', languages=[idioma])
        language.install()
        self._ = language.gettext

    def set_handler(self, handler: CheatViewHandler) -> None:
        self.handler = handler

    def on_activate(self, app: Gtk.Application) -> None:
        self.build(app, self._("Write :help for usage help"))

    # A cada entrada del formulario le anadimos la etiqueta correspondiente
    def build(self, app: Gtk.Application, result) -> None:
        self.window = win = Gtk.ApplicationWindow(
            title="Cheat.sh",
        )
        app.add_window(win)
        win.connect("destroy", lambda win: win.close())

        box = Gtk.Box(
            orientation=Gtk.Orientation.VERTICAL,
            spacing=12
        )

        img = Gtk.Image()
        img.set_from_file(PATH_SRC + '/cheat_logo.png')
        box.pack_start(img, False, False, 0)

        # anadir cuadro y boton de busqueda
        box.pack_start(self.search_input(), False, False, 0)

        # anadir label para los resultados
        box.pack_start(self.result_output(result), False, False, 0)

        win.add(box)
        win.show_all()
        win.present()

    def search_input(self) -> Gtk.Widget:
        boxg = Gtk.Box(
            orientation=Gtk.Orientation.HORIZONTAL,
            spacing=20,
        )

        box = Gtk.Box(
            orientation=Gtk.Orientation.HORIZONTAL,
            spacing=20,
        )
        box.pack_start(
            Gtk.Label(label="$ curl cheat.sh/ "), False, False, 0)

        # creado de entry y completion
        self.entry = Gtk.Entry()

        store = Gtk.ListStore(str)
        fill_store(store)

        completion = Gtk.EntryCompletion()
        completion.set_model(store)
        self.entry.set_completion(completion)
        completion.set_text_column(0)

        box.set_center_widget(self.entry)

        self.spinner = Gtk.Spinner()
        box.pack_end(self.spinner, False, False, 0)

        box.pack_end(
            button := Gtk.Button(label=self._("Buscar")), False, False, 0)

        # Anadimos un placeholder con un ejemplo
        self.entry.set_placeholder_text(":help")

        button.connect('clicked', lambda _wg: self.handler.on_search(self.entry.get_text()))
        self.entry.connect('activate', lambda _wg: self.handler.on_search(self.entry.get_text()))

        self.entry.connect('changed', lambda _wg: self.on_entry_changed())

        boxg.set_center_widget(box)
        return boxg

    def start_spinner(self):
        self.spinner.start()

    def stop_spinner(self):
        self.spinner.stop()

    def on_entry_changed(self):
        if self.entry.get_text() == "":
            self.entry.get_style_context().add_class('error')
        else:
            self.entry.get_style_context().remove_class('error')

    def result_output(self, result) -> Gtk.Widget:
        box = Gtk.ScrolledWindow()
        box.set_propagate_natural_height(1)
        box.set_min_content_height(300)
        box.set_min_content_width(700)

        self.label = Gtk.Label(result)
        box.add(self.label)

        return box

    def update(self, result):
        text_fallo = self._("*************  Comando no existente   ***************\n"
                            "******* Consulte con :help como usar Cheat.sh *******\n\n")
        text_connection_failed = self._("*************  CONEXIÓN FALLIDA   ***************\n"
                                        "********** Revisa tu acceso a internet **********\n\n")
        text_running = self._("Ya se está realizando una consulta...")
        self.entry.set_text("")  # vaciar el entry
        if result == "error":
            self.label.set_text(text_fallo)
        else:
            if result == "connerror":
                self.label.set_text(text_connection_failed)
            else:
                if result == "running":
                    self.label.set_text(text_running)
                else:
                    self.label.set_text(result)
