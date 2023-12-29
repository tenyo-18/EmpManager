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
Running the project (feel free to launch it via Intellij)
```
cd target && java -jar EmpManager-0.0.1-SNAPSHOT.jar
```

## Authentication

The project supports in memory authentication and there's an endpoint to perform a login request. There's already an user configured as admin with the following username and pass.

```
curl -i -X POST -d username=admin -d password=admin -c /tmp/cookies.txt \
http://localhost:8080/login

```

The above request returns 200 and a cookie to be used to access the resources of the API.

## API documentation
Application exposes following REST endpoints.

| Http method | Endpoint                                               | Description                                                  |
|-------------|--------------------------------------------------------|--------------------------------------------------------------|
| POST        | /api/employees                                         | Creates a new employee hierarchy                             |
| GET         | /api/employees/supervisors?name={name}&levels={levels} | Retrieves the employee's supervisor for the number of levels |


### POST /api/employees

Creates a new employee hierarchy

Sample request:
```json
{
    "Pete": "Nick",
    "Barbara": "Nick",
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
				"Barbara": {}
			}
		}
	}
}
```


### POST /api/employees/supervisors?name={name}&levels={levels}

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