# Reto Práctico – Maestría Clean Code y Arquitecturas Modernas

Este directorio contiene plantillas de código y scripts de ejemplo para completar el reto práctico descrito en el documento de la Maestría Clean Code.  Se proporcionan archivos en **Java**, **Python** y **Angular (TypeScript)** para las distintas actividades del reto.  Puedes usar estos ejemplos como punto de partida para refactorizar, agregar pruebas, modelar el dominio, aplicar principios SOLID, detectar code smells y diseñar una arquitectura limpia.

## Estructura de carpetas

```
reto_clean_code/
├── README.md              # Este archivo de documentación
├── java/                  # Archivos de ejemplo en Java
│   ├── step1_procedural.java
│   ├── step1_oop.java
│   ├── step2_discount.java
│   ├── step2_discount_test.java
│   ├── domain/            # Modelo de dominio para DDD
│   │   ├── Producto.java
│   │   ├── Carrito.java
│   │   ├── ItemCarrito.java
│   │   ├── Pedido.java
│   │   └── Pago.java
│   ├── step4_informe_original.java
│   ├── step4_informe_refactor.java
│   ├── step5_report.java
│   └── step5_report_refactor.java
├── python/                # Archivos de ejemplo en Python
│   ├── step1_procedural.py
│   ├── step1_oop.py
│   ├── step2_discount.py
│   ├── test_step2_discount.py
│   ├── domain/
│   │   ├── producto.py
│   │   ├── carrito.py
│   │   ├── item_carrito.py
│   │   ├── pedido.py
│   │   └── pago.py
│   ├── step4_informe_original.py
│   ├── step4_informe_refactor.py
│   ├── step5_report.py
│   └── step5_report_refactor.py
└── angular/               # Archivos de ejemplo en TypeScript/Angular
    ├── step1-procedural.ts
    ├── step1-oop.ts
    ├── step2-discount.ts
    ├── step2-discount.spec.ts
    ├── domain/
    │   ├── producto.ts
    │   ├── carrito.ts
    │   ├── itemCarrito.ts
    │   ├── pedido.ts
    │   └── pago.ts
    ├── step4-informe-original.ts
    ├── step4-informe-refactor.ts
    ├── step5-report.ts
    └── step5-report-refactor.ts
```

## Uso

1. **Paso 1:** Examina los archivos `step1_procedural` y refactorízalos a la versión orientada a objetos (`step1_oop`) en el lenguaje que hayas elegido.  Añade comentarios explicando por qué la nueva versión mejora la encapsulación y la mantenibilidad.

2. **Paso 2:** Utiliza los archivos `step2_discount` como punto de partida para escribir pruebas unitarias.  Los tests de ejemplo (`step2_discount_test.java`, `test_step2_discount.py` y `step2-discount.spec.ts`) demuestran cómo validar el comportamiento de la función.  Amplía las pruebas y aplica TDD para refactorizar la lógica usando colecciones o diccionarios.

3. **Paso 3:** En la carpeta `domain` encontrarás clases básicas (`Producto`, `Carrito`, `Pedido` y `Pago`).  Amplíalas y distribúyelas en *contextos delimitados* de acuerdo con los principios de DDD.  Añade un glosario de términos que se usará como lenguaje ubicuo.

4. **Paso 4:** El archivo `step4_informe_original` muestra una clase que viola principios SOLID al mezclar responsabilidades.  Refactoriza la clase siguiendo el ejemplo de `step4_informe_refactor`, creando interfaces e inyectando dependencias.

5. **Paso 5:** Analiza el método `generarReporte` de `step5_report` y detecta olores de código.  Propón una refactorización que respete KISS, DRY y YAGNI, y registra tus observaciones en `step5_report_refactor`.

6. **Paso 6:** Para el diseño de una arquitectura limpia, utiliza las clases de dominio como núcleo y separa los casos de uso, adaptadores y detalles externos en tu propio proyecto.  Puedes tomar los archivos de ejemplo como base para crear controladores web, servicios de aplicación y repositorios desacoplados.

Cada carpeta de lenguaje es independiente; si te centras en Python, no necesitas usar los ejemplos de Java o TypeScript.  La estructura busca que puedas ver cómo se traducen los mismos principios en distintos lenguajes.
