

# Required Setup
Java >=1.8

Apache Maven >= 3.6.3

Nodejs LTS

Npm

Serverless framework

https://www.serverless.com/framework/docs/providers/aws/guide/quick-start/

# Services:
STATS

curl  -X GET https://a7xmzzm11k.execute-api.us-east-1.amazonaws.com/dev/stats 

MUTANT

curl -v -X POST https://a7xmzzm11k.execute-api.us-east-1.amazonaws.com/dev/mutant -d '{ "adn": "['ATGCGA','CAGTGC','TTATGT','AGAATG','CCTCTA','TCACTA']"}'


# **Nivel 1:**
Programa (en cualquier lenguaje de programación) que cumpla con el método pedido por Magneto.

RTA: Se crea algoritmo en Java para dar soporte al metodo que pide magneto.

# **Nivel 2:**
Crear una API REST, hostear esa API en un cloud computing libre (Google App Engine, Amazon AWS, etc), crear el servicio “/mutant/” en donde se pueda detectar si un humano es mutante enviando la secuencia de ADN mediante un HTTP POST con un Json el cual tenga el siguiente formato:

POST → /mutant/ { “dna”:["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"] }

RTA:  Se crea el siguiente endpoint en AWS para soportar para el metodo que solicita magneto

curl -v -X POST https://a7xmzzm11k.execute-api.us-east-1.amazonaws.com/dev/mutant -d '{ "adn": "['ATGCGA','CAGTGC','TTATGT','AGAATG','CCTCTA','TCACTA']"}'

En caso de verificar un mutante, debería devolver un HTTP 200-OK, en caso contrario un 403-Forbidden

# **Nivel 3:**
Anexar una base de datos, la cual guarde los ADN’s verificados con la API. Solo 1 registro por ADN.

Exponer un servicio extra “/stats” que devuelva un Json con las estadísticas de las verificaciones de ADN: {"count_mutant_dna" : 40, "count_human_dna" : 100, "ratio" : 0.4 } Tener en cuenta que la API puede recibir fluctuaciones agresivas de tráfico (Entre 100 y 1 millón de peticiones por segundo). Test-Automáticos, Code coverage > 80%.

RTA: Se plantea para el ejercicio una arquitectura serverless para soportar el nivel de trafico de la aplicacion facilmente y adicionalmente guardar costos a la hora de desplegar la aplicacion. Esta aplicacion corre en Lambda AWS, principalmente construida en el lenguaje JAVA basada en el framework serverless.

Se crea una tabla en dynamodb atraves de un template en cloudformation, en esta se guarda la informacion del adn y el resultado del algoritmo para saber si es un mutante o no.

Endpoint  del servicio stats:
curl  -X GET https://a7xmzzm11k.execute-api.us-east-1.amazonaws.com/dev/stats  

