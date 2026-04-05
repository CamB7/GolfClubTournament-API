**Golf Tournament API**

***Supported API endpoints***


****Members****

http://localhost:8080/api/members
http://localhost:8080/api/members/{memberId} 
http://localhost:8080/api/members?name={partialName}
http://localhost:8080/api/members?membershipType={type}
http://localhost:8080/api/members?phone={phoneNumber}
http://localhost:8080/api/members?startDate=YYYY-MM-DD 
http://localhost:8080/api/members?tournamentStartDate=YYYY-MM-DD


****Tournaments****

http://localhost:8080/api/tournaments 
http://localhost:8080/api/tournaments/{tournamentId} 
http://localhost:8080/api/tournaments?startDate=YYYY-MM-DD
http://localhost:8080/api/tournaments?location={partialLocation}
http://localhost:8080/api/tournaments?memberId={memberId}


****Tournament participants****

POST http://localhost:8080/api/tournaments/{tournamentId}/members/{memberId}
DELETE http://localhost:8080/api/tournaments/{tournamentId}/members/{memberId}
GET http://localhost:8080/api/tournaments/{tournamentId}/members 


****Run the project with Docker****

Start the local stack (app + MySQL) using Docker Compose: docker compose up --build This will build the app image and start two services: app and db (MySQL). The app defaults to listening on port 8080 on the host.

Run the stack in detached/background mode: docker compose up -d --build

View logs: docker compose logs -f app (tail application logs) docker compose logs -f db (tail database logs)

Stop and remove containers (and optionally volumes): docker compose down docker compose down -v (removes named volumes, including database data)
