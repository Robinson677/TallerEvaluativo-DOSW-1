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

![alt text](docs/imagenes/compila.png)

✅*Compilaciòn de SonarQube:*
![alt text](docs/imagenes/sonar.png)
![alt text](docs/imagenes/sonar2.png)
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

## Historial de commits