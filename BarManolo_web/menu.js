var fecha = "dd/mm/aaaa";
var nombre = "";
var num = 0;
var menu = "menu_dia";
var m;
var e;

var r = true;

function updateMenu(){
	m = document.getElementById("opcion").value;
	switch (m) {
		case "Menú del día":
			menu = "menu_dia.jfif";
			break;
		case "Menú Vegano":
			menu = "vegano.jpg";
			break;
		case "Menú infantil":
			menu = "niños.webp";
			break;
	
		default:
			break;
	}
}

function mostrarReservas(){
	var res = document.createElement('li');
	res.appendChild(document.createTextNode('***    TUS RESERVAS:    ***'));
	document.querySelector('ul').appendChild(res);
}

function comprobarValores(){
	if (num>10 || num<1) {
		document.getElementById("err1").innerText = "Número de comensales incorrecto.";
		e = false;
	} else {
		document.getElementById("err1").innerText = "";
	}
	if (nombre == "") {
		document.getElementById("err2").innerText = "Nombre incorrecto.";
		e = false;
	} else {
		if(/\d/.test(nombre)){
			document.getElementById("err2").innerText = "El nombre contiene números";
			e = false;
		}else {
			document.getElementById("err2").innerText = "";
		}
	}
	if (fecha == "dd/mm/aaaa") {
		document.getElementById("err3").innerText = "Fecha no puede ser anterior a la actual.";
		e = false;
	} else {
		document.getElementById("err3").innerText = "";
	}

	
}

function crearElemento(){

	nombre = document.getElementById("nombre").value;
	num = document.getElementById("personas").value;
	fecha = document.getElementById("fecha").value;
	updateMenu();

	e = true;
	comprobarValores();
	if (e) {
		if (r==true) {
			mostrarReservas();
			r=false;
		}
	
		var elemento = document.createElement('li');
	
		//añadir imagen del menu seleccionado directamente a la lista
		var menuimg = document.createElement('img');
		menuimg.src = "i/"+menu;
		menuimg.width = 400;
		document.querySelector('ul').appendChild(menuimg);
	
		if (fecha == "") {
            elemento.appendChild(document.createTextNode('1.Reserva a nombre de ' + nombre
                + ' para ' + num + ' personas con el menú ' + m));
        } else {
            elemento.appendChild(document.createTextNode('1.Reserva a nombre de ' + nombre + ' el dia ' + fecha
                + ' para ' + num + ' personas con el menú ' + m));//añadir texto con variables
        }
	
		document.querySelector('ul').appendChild(elemento);//añadir el elemento a la lista
	}
}