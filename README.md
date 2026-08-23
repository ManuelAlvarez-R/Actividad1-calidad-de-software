Documento de Sustentación — Calculadora Graydoll
Asignatura: Calidad de Software
Proyecto: Calculadora Graydoll (Actividad 1)
Integrantes: Manuel Felipe Alvarez Rua · Carlos Andrey Henao Rincón
Fecha: 22 de agosto de 2026
---
1. Resumen ejecutivo
Este documento sustenta de forma completa el proyecto Calculadora Graydoll, una
aplicación de escritorio escrita en Java que realiza las cuatro operaciones
aritméticas básicas (suma, resta, multiplicación y división). El propósito
académico del proyecto no es la calculadora en sí, sino demostrar buenas
prácticas de calidad de software: una arquitectura desacoplada, aplicación de
principios SOLID, y —sobre todo— una batería de pruebas automatizadas que
cubre el camino feliz, el camino excepcional, casos avanzados y desempeño.
La suite consta de 34 pruebas unitarias organizadas en 4 clases. En la
ejecución realizada para este documento las 34 pruebas pasaron (0 fallos, 0
omitidas, 100 % de éxito), confirmando que la lógica de negocio es correcta,
robusta ante errores y eficiente.
---
2. ¿Qué hace el programa?
La Calculadora Graydoll es una calculadora de escritorio con interfaz gráfica que
permite al usuario:
Función	Descripción
Suma (`+`)	Suma dos operandos de tipo `double`.
Resta (`-`)	Resta el segundo operando del primero.
Multiplicación (`*`)	Multiplica dos operandos.
División (`/`)	Divide el primer operando entre el segundo. Rechaza la división por cero con un error controlado.
Signo (`+/-`)	Invierte el signo del número mostrado.
Decimal (`.`)	Permite ingresar números con parte decimal.
Borrar (`C`)	Reinicia el estado de la calculadora.
Retroceso (`⌫`)	Elimina el último dígito ingresado.
La operatoria funciona de forma encadenada: al presionar un nuevo operador
mientras hay una operación pendiente, se resuelve la anterior automáticamente
(por ejemplo, `2 + 3 * 4` evalúa `2 + 3` antes de aplicar `* 4`). Cuando el
usuario intenta dividir por cero, la interfaz muestra un cuadro de diálogo de
error ("No se puede dividir por cero") y limpia el estado, en lugar de mostrar
`Infinity` o `NaN`.
---
3. Tecnología implementada
Tecnología	Versión	Uso en el proyecto
Java	17	Lenguaje de programación y bytecode objetivo. Se usan características modernas como `switch` de expresión (`CalculatorApp.apply`).
Gradle	8.11.1 (vía wrapper)	Automatización de compilación, ejecución de pruebas y arranque de la app. No requiere instalación previa gracias a `gradlew`.
JUnit 5 (Jupiter)	5.11.4	Framework de pruebas unitarias. Se emplean `@Test`, `@Nested`, `@ParameterizedTest`, `@CsvSource`, `@Timeout`, `@BeforeEach` y `@DisplayName`.
Swing	JDK	Biblioteca de interfaz gráfica (ventana, botones, campo de texto).
Git / GitHub	—	Control de versiones y colaboración.
¿Por qué estas tecnologías?
Java 17 es una versión LTS (soporte a largo plazo), estable y ampliamente
usada en entornos empresariales y académicos.
Gradle con wrapper garantiza que cualquier evaluador reproduzca exactamente
la misma versión de build sin instalar nada (`.\gradlew.bat test`), lo que
elimina el clásico problema de "en mi máquina funciona".
JUnit 5 es el estándar de facto para pruebas en Java. Su modelo de
anotaciones permite expresar de forma declarativa qué se prueba y bajo qué
condiciones, y soporta pruebas parametrizadas y de tiempo límite que son clave
para este trabajo.
---
4. Arquitectura del software
El diseño separa tres responsabilidades: presentación, dominio (lógica) y
pruebas. La pieza central del diseño es que `Calculator` no calcula:
delega cada operación en una implementación de la interfaz `Operation`.
```
Presentación         Dominio                              Pruebas
-----------          -------------------------            ----------------------
CalculatorApp  --->  Calculator (fachada)          <---  CalculatorHappyPathTest
(Swing)                 |                            <---  CalculatorExceptionalPathTest
                        v                            <---  CalculatorAdvancedTest
                    Operation (interfaz)             <---  CalculatorPerformanceTest
                    /    |      |        \
              Addition Subtraction Multiplication Division
                                                     |
                                          DivisionByZeroException
```
4.1 Componentes
Capa	Clase(s)	Responsabilidad
Presentación	`CalculatorApp`	Ventana Swing. Gestiona el estado de la UI (dígitos, operador pendiente) y delega toda la aritmética en `Calculator`. No contiene fórmulas.
Fachada	`Calculator`	Expone `add`, `subtract`, `multiply`, `divide`. Orquesta, no calcula.
Contrato	`Operation`	Interfaz funcional con un solo método: `double apply(double left, double right)`.
Operaciones	`Addition`, `Subtraction`, `Multiplication`, `Division`	Una clase por operación; cada una con una única responsabilidad.
Error de dominio	`DivisionByZeroException`	Excepción no verificada que representa el fallo controlado de dividir por cero.
4.2 Principios de diseño aplicados
SRP (Responsabilidad Única): cada clase de operación hace una sola cosa; la
GUI no mezcla lógica de cálculo con lógica de presentación.
OCP (Abierto/Cerrado): para agregar una operación nueva (p. ej. potencia)
basta con crear una clase que implemente `Operation`, sin modificar las
existentes.
DIP (Inversión de Dependencias): `Calculator` depende de la abstracción
`Operation`, no de clases concretas.
Patrón Strategy: cada operación es una estrategia intercambiable.
Inyección de dependencias: el constructor de `Calculator` permite inyectar
cualquier `Operation`, lo que habilita el uso de dobles de prueba (fakes) en
los tests.
Esta arquitectura es precisamente lo que hace testeable el sistema y da
sentido a la suite de pruebas descrita a continuación.
---
5. Estrategia de pruebas: ¿por qué y qué tipos?
La calidad de software se sustenta en la evidencia de que el sistema se comporta
como se espera. Por eso el proyecto no tiene "una prueba", sino cuatro
categorías complementarias, cada una respondiendo a una pregunta distinta:
Tipo de prueba	Clase	Pregunta que responde	¿Por qué es necesaria?
Happy Path (camino feliz)	`CalculatorHappyPathTest`	¿Funciona con entradas válidas y normales?	Confirma que la funcionalidad básica es correcta. Es la línea base de confianza.
Exceptional Path (camino excepcional)	`CalculatorExceptionalPathTest`	¿Falla de forma controlada ante entradas inválidas?	Un software de calidad no se rompe: gestiona los errores. Verifica la división por cero.
Pruebas avanzadas	`CalculatorAdvancedTest`	¿Se cumple el comportamiento en muchos casos, bordes y contratos?	Aumenta la cobertura con pruebas parametrizadas, casos límite y verificación de contratos (Liskov) e inyección de dependencias.
Performance (desempeño)	`CalculatorPerformanceTest`	¿Es lo bastante rápido y sin regresiones de tiempo?	Detecta degradaciones groseras de rendimiento de forma automática.
Todas las pruebas siguen el patrón AAA (Arrange – Act – Assert): se preparan
los datos, se ejecuta la operación y se verifica el resultado. Esto las hace
legibles y mantenibles.
Un detalle técnico importante: las comparaciones usan una tolerancia (DELTA)
—`0.0001` en las pruebas simples y `1e-9` en las avanzadas— porque los números en
coma flotante (`double`) no son exactos (por ejemplo, `0.1 + 0.2` no da
exactamente `0.3`). Comparar con delta es la práctica correcta y demuestra
comprensión de la aritmética de punto flotante.
---
6. Detalle exhaustivo de cada prueba
> **Nota sobre resultados:** la sección 7 documenta la ejecución real de toda la
> suite. **Las 34 pruebas pasaron.** En las tablas siguientes, la columna
> *Resultado* indica **PASA** para cada una, junto con la explicación de por qué
> ese resultado es el correcto.
6.1 `CalculatorHappyPathTest` — Camino feliz (8 pruebas)
Verifica que cada operación produce el resultado matemático correcto con entradas
válidas. Usa `@BeforeEach` para crear una `Calculator` nueva antes de cada prueba
(aislamiento entre tests).
#	Prueba	Entrada	Esperado	Resultado	Explicación
1	`addPositiveIntegers`	`2 + 3`	`5`	PASA	Suma básica de enteros positivos. Caso más elemental.
2	`addDecimals`	`1.5 + 2.25`	`3.75`	PASA	Verifica la suma con decimales; valida el manejo de `double`.
3	`subtractNumbers`	`10 - 4`	`6`	PASA	Resta simple con resultado positivo.
4	`subtractResultingNegative`	`3 - 8`	`-5`	PASA	Comprueba que la resta soporta resultados negativos.
5	`multiplyNumbers`	`6 * 7`	`42`	PASA	Multiplicación básica.
6	`multiplyByOne`	`9.5 * 1`	`9.5`	PASA	Elemento identidad de la multiplicación: multiplicar por 1 no cambia el valor.
7	`divideNumbers`	`20 / 4`	`5`	PASA	División exacta sin residuo.
8	`addZero`	`12 + 0`	`12`	PASA	Identidad aditiva: sumar 0 no altera el valor.
Por qué son necesarias: son la garantía mínima de que el producto hace lo que
promete. Incluyen deliberadamente casos de identidad (multiplicar por 1, sumar
0) y signos (resultado negativo) para no limitarse a un único escenario trivial.
6.2 `CalculatorExceptionalPathTest` — Camino excepcional (3 pruebas)
Verifica que la división por cero lanza `DivisionByZeroException` en lugar de
devolver `Infinity`/`NaN`. Usa `assertThrows`, que verifica tanto que se lanza la
excepción como su tipo.
#	Prueba	Entrada	Esperado	Resultado	Explicación
1	`divideByZeroThrows`	`10 / 0`	Lanza `DivisionByZeroException` con mensaje "No se puede dividir por cero"	PASA	Además de la excepción, verifica el mensaje exacto, garantizando comunicación clara del error.
2	`divideByNegativeZeroThrows`	`5 / -0.0`	Lanza `DivisionByZeroException`	PASA	Caso sutil: en `double` existe `-0.0`. La comprobación `right == 0.0` en `Division` también captura `-0.0` (por eso pasa). Demuestra rigor.
3	`zeroDividedByZeroThrows`	`0 / 0`	Lanza `DivisionByZeroException`	PASA	`0/0` matemáticamente es indeterminado (`NaN` en flotante). El código lo rechaza como inválido.
Por qué son necesarias: un software de calidad debe fallar de forma
controlada y predecible. Estas pruebas evidencian que la única vía de fallo
de la aritmética (dividir por cero, en sus tres variantes: cero, cero negativo e
indeterminado) está cubierta.
6.3 `CalculatorAdvancedTest` — Pruebas avanzadas (21 pruebas)
Organizada con `@Nested` en cinco grupos temáticos. Emplea `@ParameterizedTest`
con `@CsvSource` para ejecutar el mismo test con múltiples juegos de datos, y una
tolerancia estricta `DELTA = 1e-9`.
Grupo A — Suma parametrizada (5 pruebas):
Caso	Esperado	Resultado	Explicación
`0 + 0`	`0`	PASA	Neutro.
`1 + 2`	`3`	PASA	Positivos.
`-4 + 10`	`6`	PASA	Mezcla de signos.
`-3.5 + -1.5`	`-5`	PASA	Dos negativos con decimales.
`0.1 + 0.2`	`0.3`	PASA	Caso clave de punto flotante: `0.1+0.2` da `0.30000000000000004`; pasa gracias a la tolerancia `1e-9`.
Grupo B — Resta parametrizada (4 pruebas):
Caso	Esperado	Resultado	Explicación
`5 - 5`	`0`	PASA	Resultado cero.
`0 - 7`	`-7`	PASA	Genera negativo.
`-2 - -8`	`6`	PASA	Resta de negativos.
`100.5 - 0.5`	`100`	PASA	Con decimales.
Grupo C — Multiplicación parametrizada (4 pruebas):
Caso	Esperado	Resultado	Explicación
`0 * 99`	`0`	PASA	Absorbente del 0.
`-3 * 4`	`-12`	PASA	Signo negativo.
`-2 * -5`	`10`	PASA	Negativo × negativo = positivo.
`2.5 * 4`	`10`	PASA	Con decimal.
Grupo D — División parametrizada + borde (5 pruebas):
Caso	Esperado	Resultado	Explicación
`9 / 3`	`3`	PASA	División exacta.
`-8 / 2`	`-4`	PASA	Con signo.
`1 / 4`	`0.25`	PASA	Resultado fraccionario.
`0 / 5`	`0`	PASA	Cero dividido por número válido.
`divideByTinyNumber` (`1 / 1e-12`)	`1e12`	PASA	Caso límite: un número muy pequeño no se confunde con cero; la división procede normalmente. Valida que el umbral de "cero" es estricto (`== 0.0`) y no una vecindad.
Grupo E — Contrato de `Operation` (Liskov) (2 pruebas):
Prueba	Esperado	Resultado	Explicación
`eachImplementationApplies`	Toda implementación responde `apply` sin `NullPointerException`	PASA	Verifica el principio de sustitución de Liskov (LSP): cualquier `Operation` es intercambiable y honra el contrato.
`divisionContractOnZero`	`Division.apply(1, 0)` lanza `DivisionByZeroException`	PASA	Confirma que el contrato de error se cumple a nivel de la operación concreta, no solo de la fachada.
Grupo F — Inyección de dependencias (1 prueba):
Prueba	Esperado	Resultado	Explicación
`addUsesInjectedOperation`	Con un doble `fakeAdd` que devuelve `123`, `Calculator.add(1,1)` devuelve `123`	PASA	Prueba con doble (fake): demuestra que `Calculator` delega y no calcula por su cuenta. Si `Calculator` sumara internamente, devolvería `2` y la prueba fallaría. Valida la arquitectura desacoplada.
Por qué son necesarias: elevan la cobertura de casos (positivos, negativos,
decimales, ceros, bordes) con poco código gracias a la parametrización, y —lo más
valioso— prueban el diseño, no solo los números: verifican el contrato de la
interfaz y la inyección de dependencias.
6.4 `CalculatorPerformanceTest` — Desempeño (2 pruebas)
Pruebas de tiempo acotado. No son un benchmark riguroso (para eso se usaría
JMH), pero detectan regresiones groseras de rendimiento de forma automática.
#	Prueba	Carga	Límite	Resultado	Explicación
1	`mixedOperationsCompleteUnderTimeout`	10 000 iteraciones × 4 operaciones = 40 000 operaciones	`@Timeout` 100 ms	PASA	Si el bloque supera 100 ms, JUnit falla automáticamente. Verifica rendimiento del conjunto de operaciones.
2	`oneMillionAdditionsStayFast`	1 000 000 de sumas	`assertTimeout` 200 ms	PASA	Prueba de estrés ligera. `assertTimeout` ejecuta el bloque y verifica que termine dentro del plazo.
Diferencia entre las dos técnicas: `@Timeout` (prueba 1) es una anotación
declarativa a nivel de método; `assertTimeout` (prueba 2) es una aserción
programática dentro del cuerpo del test. Incluir ambas demuestra el dominio de
las dos formas de acotar tiempo en JUnit 5.
Por qué son necesarias: garantizan que futuras modificaciones no introduzcan
lentitud inaceptable. Son una red de seguridad frente a regresiones de
rendimiento.
---
7. Resultados de la ejecución
La suite se compiló y ejecutó sobre JDK Temurin 21 con JUnit Jupiter. El
resumen consolidado fue:
Métrica	Valor
Tests encontrados	34
Tests ejecutados	34
Exitosos	34
Fallidos	0
Omitidos	0
Tasa de éxito	100 %
Distribución por clase:
Clase	Tests	Estado
`CalculatorHappyPathTest`	8	✅ Todos pasan
`CalculatorExceptionalPathTest`	3	✅ Todos pasan
`CalculatorAdvancedTest`	21	✅ Todos pasan
`CalculatorPerformanceTest`	2	✅ Todos pasan
Total	34	✅ 100 %
Para reproducir la ejecución con la configuración oficial del proyecto (Gradle
8.11.1, JUnit 5.11.4), desde la raíz del repositorio:
```bash
.\gradlew.bat test --console=plain
```
El reporte HTML se genera en `build/reports/tests/test/index.html`.
---
8. Interpretación de los resultados
La lógica es correcta: los 8 tests de camino feliz y los 17 casos
parametrizados avanzados confirman que las cuatro operaciones producen el
resultado matemático esperado, incluyendo signos, decimales y elementos neutros.
El manejo de errores es robusto: los 3 tests de camino excepcional más el
test de contrato de división demuestran que la división por cero (en sus
variantes `0`, `-0.0` y `0/0`) se rechaza de forma controlada y con un mensaje
claro, sin devolver valores inválidos.
El diseño es sólido y testeable: las pruebas de contrato (Liskov) y de
inyección de dependencias validan que la arquitectura Strategy + DIP funciona:
las operaciones son intercambiables y `Calculator` realmente delega.
El rendimiento es adecuado: 40 000 operaciones mixtas en <100 ms y 1 000 000
de sumas en <200 ms confirman que no hay cuellos de botella.
La aritmética de punto flotante se maneja correctamente: el uso de
tolerancias (`DELTA`) y casos como `0.1 + 0.2` evidencian comprensión de las
limitaciones del tipo `double`.
En conjunto, la suite no solo verifica que las cuentas dan bien, sino que
sustenta la calidad del software: corrección, robustez, buen diseño y
desempeño, que son los cuatro pilares evaluados en la asignatura.
---
9. Conclusiones
El proyecto cumple su objetivo académico: es un caso pequeño pero completo de
software de calidad, con arquitectura desacoplada y pruebas exhaustivas.
La cobertura de pruebas es integral: abarca casos válidos, inválidos,
límite, de contrato y de rendimiento, siguiendo el patrón AAA.
La ejecución arroja 34/34 pruebas exitosas, lo que respalda con evidencia
objetiva que la aplicación es correcta, robusta y eficiente.
La separación entre lógica (`Calculator`/`Operation`) e interfaz
(`CalculatorApp`) es lo que hace posible probar el 100 % de la lógica de
negocio de forma automatizada, independientemente de la GUI.
```
