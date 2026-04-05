*Golf Tournament API*

***Supported API endpoints***


****Members****

Create a member POST http://localhost:8080/api/members Content-Type: application/json Body example (JSON): {"name":"Alice","address":"123 Main St","email":"alice@example.com","membershipType":"GOLD","phoneNumber":"555-1234","membershipStartDate":"2026-01-01","membershipDurationMonths":12} Response: created Member object (includes id)
Get a single member GET http://localhost:8080/api/members/{memberId} Example: GET http://localhost:8080/api/members/3
List / search members (all query params are optional; combine only one at a time in current implementation) GET http://localhost:8080/api/members?name={partialName} GET http://localhost:8080/api/members?membershipType={type} GET http://localhost:8080/api/members?phone={phoneNumber} GET http://localhost:8080/api/members?startDate=YYYY-MM-DD (search by membershipStartDate) GET http://localhost:8080/api/members?tournamentStartDate=YYYY-MM-DD (returns members who participate in tournaments that start on that date) Example: GET http://localhost:8080/api/members?tournamentStartDate=2026-06-15


****Tournaments****

Create a tournament POST http://localhost:8080/api/tournaments Content-Type: application/json Body example (JSON): {"startDate":"2026-06-15","endDate":"2026-06-16","location":"River View","entryFee":50,"cashPrizeAmount":500} Response: created Tournament object (includes id)
Get a single tournament GET http://localhost:8080/api/tournaments/{tournamentId} Note: members on Tournament is ignored in default JSON to avoid recursion; use the dedicated participants endpoint below.
List / search tournaments GET http://localhost:8080/api/tournaments?startDate=YYYY-MM-DD GET http://localhost:8080/api/tournaments?location={partialLocation} GET http://localhost:8080/api/tournaments?memberId={memberId} (returns tournaments that include the specified member) Example: GET http://localhost:8080/api/tournaments?memberId=3


****Tournament participants****

Add a member to a tournament POST http://localhost:8080/api/tournaments/{tournamentId}/members/{memberId} No body required. Returns updated Tournament JSON.
Remove a member from a tournament DELETE http://localhost:8080/api/tournaments/{tournamentId}/members/{memberId}
List members participating in a specific tournament (explicit endpoint) GET http://localhost:8080/api/tournaments/{tournamentId}/members Example: GET http://localhost:8080/api/tournaments/5/members


****Run the project with Docker****

Start the local stack (app + MySQL) using Docker Compose: docker compose up --build This will build the app image and start two services: app and db (MySQL). The app defaults to listening on port 8080 on the host.
Run the stack in detached/background mode: docker compose up -d --build
View logs: docker compose logs -f app (tail application logs) docker compose logs -f db (tail database logs)
Stop and remove containers (and optionally volumes): docker compose down docker compose down -v (removes named volumes, including database data)
