# Corona++ Compiler

En esta práctica, hemos desarrollado un compilador, creando todas las fases de un lenguaje de programación a partir de los conceptos teóricos adquiridos en la asignatura
Lenguajes de Programación.

## Requerimientos

Es imprescindible tener IntelliJ IDEA y tener instalado Java 11 SDK.

## Instalación

1. Descargar el zip del proyecto o clonar el proyecto en Bitbucket.
```bash
git clone https://atlassian.salle.url.edu:7943/scm/ldp/grup-3.git
```
2. Abrir IntelliJ.
3. File > New > Project from existing sources...
4. Seleccionar la carpeta donde se ha clonado el proyecto.
5. Asegurar que mediante el wizard importar los módulos y seleccionar 11 como versión del SDK. Seguir el wizard hasta el final, importando todo el código en un proyecto.

## Uso

El compilador requiere 3 argumentos: el código fuente, la gramática y el diccionario. El código fuente debe estar dentro de la carpeta source_code.

Configuramos el working directory con /data ya que todos los ficheros están dentro de esta carpeta. Indicamos las rutas a los ficheros requeridos de la siguiente manera:

```bash
source_code/<código fuente> grammar.txt dictionary.json
```

Por ejemplo:
```bash
source_code/fibonacci.txt grammar.txt dictionary.json
```

## Autores
Omar Ntifi Matarín - omar.ntifi<br/>
Javier Baltazar Mérida Morales - javier.merida<br/>
Kaye Ann Ignacio Jove - kayeann.ignacio<br/>
Nicole Marie Jimenez Burayag - nicolemarie.jimenez<br/>
Carlos Mora Clavero - carlos.mora<br/>

Lenguajes de programación @ Mayo 2020
