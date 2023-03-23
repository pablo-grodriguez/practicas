#!/usr/bin/env python3

import re
import time

import requests
import textwrap
from typing import NamedTuple, Sequence, List

URL = "https://cheat.sh/"

ANSI_ESCAPE = re.compile(r'(\x9B|\x1B\[)[0-?]*[ -\/]*[@-~]')


def escape_ansi(line: str):
    return ANSI_ESCAPE.sub('', line)


class CheatEntry(NamedTuple):
    mark: str
    description: str
    commands: str
    tags: str

    def __str__(self) -> str:
        tags = "" if self.tags == "" else f"tags: {self.tags} "
        return f"({self.mark}) {tags}{self.description}\n{self.commands}".strip()


def parse_text(text: str) -> List[CheatEntry]:
    default_mark = ""
    recipes = []
    # split("\n\n") splits on blank lines.
    # :warning: Doesn't work for two consecutive blank lines
    for chunk in text.split("\n\n"):
        entry = parse_chunk(chunk)
        if entry.mark == "":
            entry = entry._replace(mark=default_mark)
        elif entry.mark.startswith("cheat"):
            default_mark = entry.mark
        recipes.append(entry)
    return recipes


def parse_chunk(chunk: str) -> CheatEntry:
    mark = ""
    tags = ""
    description = []
    commands = []
    for line in chunk.splitlines():
        if line.startswith(" "):
            mark = line.strip()
        elif line.startswith("#"):
            description.append(line[1:])
        elif line == "---":
            # Asumimos que siempre es sintácticamente correcto
            # parsear esta especie the frontmatter (si aparece)
            pass
        elif line.startswith("tags: "):
            tags = line.removeprefix("tags: ")
        else:
            commands.append(line)
    return CheatEntry(
        mark=mark,
        description="\n".join(description),
        commands="\n".join(commands),
        tags=tags
    )


def get_cheatsheet(command: str) -> Sequence[CheatEntry]:
    r = requests.get(URL + command)
    text = escape_ansi(r.text)
    if text.startswith("Unknown topic."):
        return []
    else:
        return parse_text(text)


def get_respuesta_str(self, command: str) -> str:
    respuesta = get_cheatsheet(command)
    result_str = "\n"
    for i, r in enumerate(respuesta, start=1):
        idx = f"{i}."
        result_str = result_str + (
            textwrap.indent(
                f"{idx} {r} \n\n",
                prefix="    ",
                predicate=lambda line: not line.startswith(idx)
            )
        )
    result_str = result_str + "\n"
    return result_str


class CheatModel:

    def do_search(self, command: str) -> str:
        # sabemos que command no puede ser vacio, se comprueba en presenter
        try:
            respuesta = get_respuesta_str(self, command)
        except:
            return "connerror"

        if respuesta == "\n\n":
            return "error"
        else:
            return respuesta
