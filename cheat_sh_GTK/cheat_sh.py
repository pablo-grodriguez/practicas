#!/usr/bin/env python3

import locale
from models import CheatModel
from views import CheatView
from presenters import CheatPresenter

if __name__ == '__main__':
    loc = locale.setlocale(locale.LC_ALL, '')
    
    idioma = loc.split('_', 1)[0]

    if idioma != 'es' and idioma != 'ar':
        idioma = 'en'

    CheatPresenter(
        model=CheatModel(),
        view=CheatView(idioma),
        idioma=idioma
    ).run(application_id="es.udc.fic.ipm.Cheat_sh")
