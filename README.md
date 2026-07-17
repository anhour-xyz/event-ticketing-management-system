# Event Ticketing & Management System 🎫

## Overview:
A web-based platform enabling users to create events, manage ticket sales and generate QR coded tickets for attendees, streamlining the event management and ticket distribution process
<br>

<ins> Tech Stack: Java, Spring Boot, Spring Security, PostgreSQL, Rest APIs, Docker, AWS </ins>

<br>

Users:

- Event Organizer:
  1. Create and configure the new event with details like date, venue, and ticket types so that can sell tickets to attendees.
     
     Metrics:
     - Can put event name, time, location, and venue, etc.
     - Can set up multiple ticket types with different prices.
     - Can specify total available tickets per type.
     - Event appears on platform after creation.
     
  2. Monitor and manage the ticket sales, so that can track revenue and attendees.

     Metrics:
     - Dashboard display sales metrics.
     - Can view purchase details.
     - System prevents overselling of tickets.
     - System automatically stop at specified end date
     

- Event Goner:
  1. Buy the correct ticket so that can attend and enjoy the events.

     Metrics:
     - Can search for event on platform.
     - Can browse and select different ticket types available.
     - Can purchase the chosen ticket type smoothly.

- Event Staff:
  1. Scan attendees' QR codes at entry so that can verify ticket authentically.

     Metrics:
     - Can scan QR Codes using mobile device.
     - Can display the ticket validity instantly.
     - Can prevent duplicate ticket use.
     - Can manually enter the code if QR Scan fails.

<br>

Definitions:

1. Event: A planned occasion with specific time, date, location, and venue that requires ticketing for attendence management.
2. Ticket: A digital or physical documents that grants holder access to an event, containing event details and unique QR code for validation.
3. QR Code: A machine readable code consisting of black and white squares, used to store ticket information and verify authenticity at every event entry.

## Domain Modeling:

<img width="943" height="918" alt="3df6db65e1c40d287060f4c3ea5d6416" src="https://github.com/user-attachments/assets/23552830-ce60-4d11-a898-db59a1226a5d" />


## System Design:


RestAPIs Design:

https://github.com/anhour-xyz/event-ticketing-management-system/blob/main/RestAPIs
<br>

Architecture Design:

<img width="2430" height="1316" alt="2260cf858817bbeadfac919c0f793d5a" src="https://github.com/user-attachments/assets/498f5c66-8ee7-4ffa-ad27-fbbdbf4f7070" />

