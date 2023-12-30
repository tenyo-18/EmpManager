# EmpManager
A REST API to save employee hierarchies and finding employee's managers.

## Technologies used
* Spring Boot Framework with JPA
* Spring Security
* H2 DB

## Building and Running

To build the project run the below command in project's root folder assuming maven is installed and searchable
```
mvn clean install
```
Running the project (feel free to launch it via Intellij or any IDE after importing the pom.xml as a Project)
```
cd target && java -jar EmpManager-0.0.1-SNAPSHOT.jar
```

## Authentication

The project supports in memory authentication via /login endpoint. Once logged in successfully you should be able to 
execute the /POST and /GET apis as shown below

```
curl -i -X POST -d username=admin -d password=admin http://localhost:8080/login

```


## REST API
Application exposes following REST endpoints.

| Http method | Endpoint                                               | Description                                                |
|-------------|--------------------------------------------------------|------------------------------------------------------------|
| POST        | /api/employees                                         | Create an employee hierarchy                               |
| GET         | /api/employees/supervisors?name={name}&levels={levels} | Retrieves the employee's supervisor for the number of levels |


### POST /api/employees

Sample request:
```json
{
    "Pete": "Nick",
    "Barbara": "Nick",
    "Rahul" : "Nick",
    "Nick": "Sophie",
    "Sophie": "Jonas"
}
```

Sample success response with status code 201:
```json
{
	"Jonas": {
		"Sophie": {
			"Nick": {
				"Pete": {},
                "Rahul": {},
				"Barbara": {}
			}
		}
	}
}
```

### GET /api/employees/supervisors?name={name}&levels={levels}

Retrieves the employee's managers upto a given level

Sample success response with status code 200 for name = "Barbara" and level=2
```json
{
    "Barbara": "Nick",
    "Nick": "Sophie",
    "Sophie": "Jonas"
}
```

Sample failure response with status code 500 for name = "Mike" and level-2:
```json
{
    
    "errorMessage": "Employee not found Mike",
    "requestedURI": "/api/employees/supervisors"
}
```