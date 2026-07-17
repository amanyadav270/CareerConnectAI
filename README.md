# CareerConnect AI - Campus Placement Platform

## Project Overview
CareerConnect AI is a Java Spring Boot backend platform designed to manage campus placement drives, student applications, and eligibility workflows. It features an integrated local AI assistant (Ollama) that provides advisory career guidance using verified system context.

## Core Features
* Encapsulated Student, Company, and Placement Drive management.
* Deterministic eligibility evaluation using the Strategy Pattern.
* Application state workflow with duplicate-prevention consistency.
* Context-aware local AI chat integration using the Adapter Pattern.

## Setup and Execution Commands
**1. Run the Spring Boot Application:**
Ensure you have Java 17+ and Maven installed.
```bash
mvn spring-boot:run