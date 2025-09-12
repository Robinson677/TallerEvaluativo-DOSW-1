# 🧪 TallerEvaluativo - DOSW-1 - SOLID - PROGRAMACION FUNCIONAL - PATRONES - TDD - SPRING - SCRUM

**Autor:**

- Robinson Steven Nuñez Portela

*Descripciòn:*

El cliente necesita un sistema de monitoreo de stock de productos, 
el cual le  permita agregar productos nuevos y actualizar la cantidad de productos  disponibles. 
Adicionalmente cada vez que un producto sea actualizado es necesario que se  notifique a los dos agentes que serán implementados.

**Nombre De la Rama:**

`feature/taller1_NuñezRobinson_2025-2`


---
## Prueba de ejecución taller 1, parte 1.

**Requisitos:**

Compilamos el proyecto con el comando 

```bash
mvn compile
```

![alt text](docs/imagenes/compila.png)

💻 *Compilaciòn de SonarQube:*


1. Decarga Docker Desktop
https://www.docker.com/products/docker-desktop/

2. Descargamos la imagen de SonarQube en la cmd  *Windows + r*

Se ejecuta el comando:

```bash
 docker pull sonarqube
```

![alt text](docs/imagenes/sonar.png)

3. Arrancamos el SonarQube

```bash
 docker run -d --name sonarqube \
  -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true \
  -p 9000:9000 sonarqube:latest
```

![alt text](docs/imagenes/sonar2.png)

4. Luego se configura el  *WLS* y se reinicia el pc

```bash
 wsl --update
```

5. Se genero el tocken en localhost:9000

6. Depues descargue SonarLin en IntelliJ

7. y por ultimo se ejecuto el comando para generar cobertura

```bash
 mvn clean verify
```
luego se manda el analisis a SonarQube

```bash
 mvn verify sonar:sonar -Dsonar.token=[TU_TOKEN_GENERADO]
```

y asi compilo y esta listo para las pruebas 

![alt text](docs/imagenes/sonar3.png)



---


## Parte 2 Reto ✅.

### Diagramas UML

**Diagrama de Contexto:**

![alt text](docs/uml/diagramaContexto.png)

**Descripción:**

Se muestra el sistema en general, de manera que el cliente gestiona sus cuentas
en Stock Monitoring System y al realizar modificaciones en sus productos se 
le notifica a los agentes.


*Evidencia:*
https://drive.google.com/file/d/1dnOOamdvMtpam0H8QHqb_9Mch4dJASzs/view?usp=sharing

---

**Diagrama de Casos de Uso**

![alt text](docs/uml/diagramaCasosUso.png)

*Historia de Usuario:*

- Como cliente, quiero añadir un producto con nombre, precio, stock y categoría para poder gestionar y organizar mi inventario de manera efectiva.
- Como cliente, quiero actualizar el stock de mis productos para gestionar mejor mi inventario y evitar ventas de productos agotados.
- Como sistema de monitoreo de stocks quiero validar los datos de un producto para poder asegurar consistencia en el stock.
**Aunque los agentes vayan en el sistema decidi manejarlos como actores para tener mas claro su funcionamiento**
- Como agente Advertencia quiero alertar cuando un producto tenga menos de 5 unidades para poder anticipar escasez.
- Como agente Log quiero registrar en consola cada modificación de stock para poder auditar los cambios.

*Evidencia:*
https://drive.google.com/file/d/1O8bPoTg78PquIW3rJepvR7lu-nb_FvQZ/view?usp=sharing

---

**Diagrama de Casos de Clases**

![alt text](docs/uml/diagramaClases.png)

*Evidencia:*
https://lucid.app/lucidchart/b1f47c01-1347-47e7-9033-9cace7ebe7f2/edit?viewport_loc=-464%2C114%2C3668%2C1619%2C0_0&invitationId=inv_e9d3c41d-bd45-42a3-a9d6-34593b6f6cc0

---

## TEST DRIVEN DEVELOPMENT (TDD) ✅

Primero implemento las clases modelo para luego las pruebas y hacer la clase que las centralice 

![alt text](docs/imagenes/clases1.png)
![alt text](docs/imagenes/clases2.png)
![alt text](docs/imagenes/clases3.png)
![alt text](docs/imagenes/clases4.png)
![alt text](docs/imagenes/clases5.png)
![alt text](docs/imagenes/clases7.png)
![alt text](docs/imagenes/clases8.png)
![alt text](docs/imagenes/clases9.png)

---

# Ciclo TDD - Rojo: 🔴

![alt text](docs/imagenes/rojo1.png)
![alt text](docs/imagenes/rojo2.png)
![alt text](docs/imagenes/rojo3.png)

---

# Ciclo TDD - Verde: 🟢

***Evidencia de codigo para que pasen las pruebas:*** 

- Se implemento StockService y se mejoro ManageInventory

![alt text](docs/imagenes/verde1.png)
![alt text](docs/imagenes/verde2.png)
![alt text](docs/imagenes/verde3.png)
![alt text](docs/imagenes/verde4.png)
![alt text](docs/imagenes/verde5.png)
![alt text](docs/imagenes/verde6.png)
![alt text](docs/imagenes/verde7.png)


***Pruebas en verde:***

![alt text](docs/imagenes/verdepuro.png)

---

# Ciclo TDD - Refactor: ♻

**Codigo refactorizado en MangeInventory:**

![alt text](docs/imagenes/refactor1.png)
![alt text](docs/imagenes/refactor2.png)
![alt text](docs/imagenes/refactor3.png)
![alt text](docs/imagenes/refactor4.png)

**Codigo refactorizado en StockService:**

![alt text](docs/imagenes/refactor5.png)

---

## **Principios SOLID**

***Single Responsibility Principle:***

Hicimos que el codigo para que cada clase se encargara de su respectiva tarea

- Product: solo representa los datos de un producto

- ManageInventory: gestiona la persistencia en memoria del inventario

- StockService: Permite a los usarios añadir, actualizar y notifica a los agentes

- LogAgent y WarningAgent: se encargan unicamente de reaccionar a cambios de stock

- ExecutableStockMonitoringSystem: solo coordina la interacción con el usuario

***Open/Closed Principle:***

- Si se quiere agregar un nuevo tipo de agente, solo creamos otra clase que implemente StockObserver

- No necesitamos modificar StockService, porque este trabaja con la abstracción StockObserver

***Interface Segregation Principle:***
Las interfaces implementadas pequeñas y especificas ya que Inventory define solo las operaciones de inventario y 
StockObserver define únicamente cómo reaccionar ante cambios de stock

***Dependency Inversion:***

- StockService depende de Inventory mas no de ManageInventory

- StockService depende de StockObserver mas no de LogAgent o WarningAgent{

---

## **Patrones de Diseño:**

***Observer:***

StockService notifica a los agentes LogAgent y WarningAgent cada vez que se modifica el stock y estos
agentes estan pendientes y inmediatamente notificar de forma independiente

***Repository:***

ManageInventory actúa como un repositorio en memoria para los productos ya que
encapsula la lógica de acceso a datos y evita que StockService tenga que preocuparse por como se guardan los productos

---

# **Inyección de dependencias:** 🔌

Con Spring Boot inyectamos las dependencias desde afuera, para desacoplar las clases y
que dependan de abstracciones y no de implementaciones concretas

## - @Repository: En ManageInventory  ya que es el que guarda, actualiza y consulta productos

![alt text](docs/imagenes/repository.png)

---

## - @SpringBootApplication: En Application marca, activa y configura el Spreen y hace que este registre todos los componentes, repocitorios, etc...

![alt text](docs/imagenes/aplicacionSpreen.png)

---

## - @Component: 

#### En ExecutableStockMonitoringSystem  marca la clase como un bean generico y aqui es donde se interactua con el usuario

![alt text](docs/imagenes/ejecutar1.png)
![alt text](docs/imagenes/ejecutar2.png)
![alt text](docs/imagenes/ejecutar3.png)
![alt text](docs/imagenes/ejecutar4.png)
![alt text](docs/imagenes/ejecutar5.png)
![alt text](docs/imagenes/ejecutar6.png)

---

#### Los Agentes se registran en el contenedor de Spring como beans disponibles para la inyección de dependencias.

![alt text](docs/imagenes/agent1.png)
![alt text](docs/imagenes/agent2.png)



---

## - @Service: Lo usamos en StockService para que actue como sevicio de negocio para los clientes

![alt text](docs/imagenes/servicer.png)

---

## SONARQUBE y JaCoCo ✅

Como ya hicimos la configuración de SonarQibe y Jacoco al inicio entonces
continuamos con la cobertura de pruebas, como las prubeas necesitan al menos un
80% no me pasaban entonces implemente mas clases de pruebas para validarlo 

- Para AgentsAndStockServiceTest:

![alt text](docs/imagenes/pruebas1.png)


- Implemente mas pruebas en InventoryValidationTest

![alt text](docs/imagenes/pruebas2.png)


- Finalmente para  StockMonitoringSystemTest

![alt text](docs/imagenes/pruebas3.png)

---

### Verificamos con SonarQuBe

### 1. Entonces podemos usar la terminal de intelloJ para escribir el comando que nos valida, ejecuta y compila las pruebas:

```bash
 mvn clean verify
```

#### Verificamos que si haya funcionado, y si como se puede objservar logramos alcanzar la cobertura 

![alt text](docs/imagenes/pruebas4.png)
![alt text](docs/imagenes/pruebas5.png)

---

### 2. Luego podemos usar un comando para mirar el target directemente en internet

```bash
 target/site/jacoco/index.html
```

#### Vemos que la cobertura es mayor de 80% entonces cumplimos el objetivo

![alt text](docs/imagenes/cobertura1.png)
![alt text](docs/imagenes/cobertura1.png)


![alt text](docs/imagenes/conclusion.png)







---

## EPICS - FEATURES - HU


*Features*

**Estructura Proyecto y diagramas UML**

![alt text](docs/imagenes/Tareas.png)

---

**Ciclo TDD**

![alt text](docs/imagenes/ciclo_tdd.png)

---

**Inyeccion de dependencias**

![alt text](docs/imagenes/inyeccion.png)

---

**Prubeas SonarQube y JaCoCo**
![alt text](docs/imagenes/prys.png)

---

*historias*

![alt text](docs/imagenes/historias.png)

*Cronograma y tablero:*

![alt text](docs/imagenes/cronograma.png)
![alt text](docs/imagenes/Tablero.png)



---


## Historial de commits
