from __future__ import annotations

import threading
from typing import Optional
from models import CheatModel
from views import CheatView, run


class CheatPresenter:
    def __init__(
            self,
            idioma,
            model: Optional[CheatModel] = None,
            view: Optional[CheatView] = None,
    ) -> None:
        self.model = model or CheatModel()
        self.view = view or CheatView(idioma)
        self.is_running = False


    def run(self, application_id: str) -> None:
        self.view.set_handler(self)
        run(application_id=application_id, on_activate=self.view.on_activate)

    def on_search(self, text: str) -> None:
        if text == "":
            self.view.on_entry_changed()
        else:
            if not self.is_running:
                threading.Thread(target=self._on_search, args=(text,), daemon=True).start()
            else:
                self.view.update("running")

    def _on_search(self, t: str) -> None:
        self.is_running = True
        self.view.start_spinner()
        respuesta = self.model.do_search(t)
        self.view.update(respuesta)
        self.view.stop_spinner()
        self.is_running = False
