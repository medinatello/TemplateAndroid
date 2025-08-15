# [ANDROID][MVP-02-5] Mejoras varias (MVP-02)

> Proyecto: **Android base**  
> Módulo: **[ANDROID][MVP-02-5] Mejoras varias**  
> Plataforma: **Android 14 (targetSdk 34)** · **minSdk 26** · **Kotlin**  
> Lineamientos: nativo primero, modular, sin dependencias de terceros salvo AndroidX/Google.

---

## 1) Propósito y alineación
**Objetivo**: Mejorar los estandares de la aplicación en los dos primeros modulos:
- **Variables** Hay que tener los nombres de los objetos/variables a parte de descriptivos, deben ser en ingles
- **Test por modulo** Debemos tener pruebas para cada modulo, cubrir la cobertura de la aplicación
- **Manejo string** No debemos tener los nombres de los label, fijo, debe venir desde archivos string, para que se haga facil en un futuro tener multiple idioma
- **Mejorar el menu** El menu debe ser mas actualizado, mas amigable, dejar de usar los botones y hacer algo mas actual 
- **Comentarios** Todas las clases y miembro de la clase publica debe si o si tener sus comentarios bien explicado indicando valores que recibe y deuvelve


---

## 2) Detalles

### Variables

Los modulos 1 y 2, no tenia las especificaciones donde solo los comentarios deben ser en español, lo demas, si o si debe ser en ingles, por tal motivo hay que cambiar todas:
* Variables
* Archivos
* Namespace
* Y todo aquel objeto, al ingles
Lo unico que si estaria en español, adicional al comentario, el valor de los label y otros controles visuales, deben venir desde un archivo string, y ya aca el valor si estaria en español, pero si un dia de mañana la aplicacion debe verse en ingles, alli se cambia el archivo string

### Test por modulo

Se debe evalaur toda las logicas que tiene el proyecto y cumplir con cierta cobertura asociada a sus pruebas, de esta forma, como minimo el proyecto debe tener mas del 80% de cobertura
Necesito que liste todos aquellas parte que necesite cobertura, guardar  en un md, cuales son esos modulos/metodo etc
Proceder a crear las respectivas pruebas para cubir las coberturas
En otras plataforma, existe aplicaciones como sonartqueue, (no se si esta bien escrito)  que me permite cuando quiera evaluar aparte de cobertura, como otros aspecto, a solicitud, despues que cree los test, quiero que configuremos alguna herramienta, no quiero algo pesado, algo sencillo, pero que cumpla con este punto

### Manejo de archivos string

Puede que asi no sea el nombre, pero el principio es que todos los label que salgan en el front, como otros objetos, sea manejado con archivos de recursos, creo que aca se llama string, para que esos valores sean dinsmico
ya hay una configuracion, pero quiero que se abarque el 100% y permitir cambiar el idioma segun estandares en android, para que el dia de mañana si la aplicacion se descarga en otro idioma , cumpla con los requesitos

### Mejora en menu

Es cierto que conversamos que todo debe ser sencillo, pero creo que podemos trabajar en este modulo, trabajar con menus al estilo de android, y manejo de pantalla al estilo de android del diseño que se pida ahora, entiendo la funcionalidad ya se cumple, con botones, pero quiero ya darle forma al estandar actual
Con barra de busqueda, tambien con botones para poder cambiar de modo noche a modo claro, buscar la forma como se aplicaria ese diseño usando los estandares


## 3) Cierre
Vamos a estar iterando el  punto 2, por separado, tu vas a ejecutando cada uno, luego yo lo reviso y luego de terminar la revisiones, hacemos un commit y documentar los cambios en la carpeta cases/modulo2_5
Entonces tenemos ese procedimiento
* Te digo cual session del punto 2 a trabajar
* Tu realiza las preguntas que consideres
* Si no tienes preguntas, procedes a realizar los cambios solicitados, si tienes pregunta hacemos las preguntas y respuesta necesarias
* Avisame cuando termine, para yo poder revisar los cambios
* Recuerda validar todo haciendo build clean necesarios
* Si todo esta bien, procedemos al commit y documentar los cambios en la carpeta cases/modulo2_5
* Si algo hay que acomodar, interamos
* Cuando este listo vamos al siguiente session

## 4) Documentacion estandar
Para evitar en futuros modelos que nos vuelva a pasar de que al no indicar que trabajemos las variables en ingles, quiero hacer un md que este en la raiz de cases, que tenga los principios minimos para el desarrollo
Por supuesto, que alli estara explicitamente que todo tiene que estar en ingles
Los comentarios pueden estar en español
Los label y / o otros objetos que el usuario ve, debe ser dinamicos con archivos de recursos/string
Todo debe ser estandar que indica google, tanto en diseño, como en nomenclatura, indicar los nombres exactos, hay que ser explicito para que tu en el futuro sepa que usar
Cada modulo o interaccion que haga la IA, cuando me la entregue debe haberse compilado y limpiado el proyecto
La cobertura de los commit debe ser mas del 80% por lo cual todo deberia salir con test unitarios
Trabajar el manejo de errores con try catch, si se genera un error en un nivel inferior, no se debe elevar el error a nivel raiz, no, hay que capturar el error, y pasarlo como respuesta a nivel superior, alli, se debe evaluar, si el resultado era lo esperado o un error, y si es un error, salir de ese nivel
Lo anterior es muy parecido a como se trabaja en golang, con la diferencia que nosotros si vamos a trabajar con try catch, pero no saltaremos varios niveles, esto permite llevar como debe ser un log
El tema de log, a hoy no lo hemos trabajado, pero coloca en este archivo que maneje los log el mas estandar, en el futuro cuando nos conectemos a algo con open telemetry o lo que sea (que esta proyectado en modulo futuro), alli se complementa, pero ahora la idea es que si hay algun error, tengamos aunque sea un log en consola, lo que permitira que en futuro se hagfa mas facil adaptar
Agrega a ese archivo, llamemoslo reglas, todo aquel convenio que permita que nuestro codigo sea de alta calidad









