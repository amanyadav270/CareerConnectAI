#!/usr/bin/env bash
# Re-verification script for docs/test_checklist.md rows T-13, T-18 to T-22.
# Prerequisite: the app is already running (mvn spring-boot:run) on :8080.
# Usage: bash test-evidence/run-verification.sh
# Run this, then paste the printed output (or a screenshot of it) as your
# evidence, and flip the matching row in docs/test_checklist.md to
# "Verified" yourself once you've confirmed the status codes below.

BASE="http://localhost:8080/api"

echo "=== T-13: blank/invalid student field -> expect 400 VALIDATION_ERROR ==="
curl -s -o /tmp/t13.json -w "HTTP %{http_code}\n" -X POST "$BASE/students" \
  -H "Content-Type: application/json" \
  -d '{"id":"STU-BAD","name":"","email":"not-an-email","programme":"CSE","graduationYear":2026,"cgpa":11,"activeBacklogs":-1,"skills":[]}'
cat /tmp/t13.json; echo

echo "=== T-18: drive deadline before today -> expect 400 ==="
# First create a company to reference (id below reused across steps)
curl -s -o /tmp/company.json -w "HTTP %{http_code}\n" -X POST "$BASE/companies" \
  -H "Content-Type: application/json" \
  -d '{"name":"Verify Corp","sector":"IT","description":"For test verification"}'
COMPANY_ID=$(python3 -c "import json;print(json.load(open('/tmp/company.json'))['id'])" 2>/dev/null)
echo "created company id: $COMPANY_ID"

curl -s -o /tmp/t18.json -w "HTTP %{http_code}\n" -X POST "$BASE/drives" \
  -H "Content-Type: application/json" \
  -d "{\"companyId\":\"$COMPANY_ID\",\"role\":\"Backend Engineer\",\"location\":\"Remote\",\"packageAmount\":1000000,\"deadline\":\"2020-01-01\",\"requiredSkills\":[\"Java\"],\"minCgpa\":7.0,\"maxBacklogsAllowed\":0}"
cat /tmp/t18.json; echo

echo "=== T-19: duplicate company name -> expect 409 (repeat same name) ==="
curl -s -o /tmp/t19.json -w "HTTP %{http_code}\n" -X POST "$BASE/companies" \
  -H "Content-Type: application/json" \
  -d '{"name":"Verify Corp","sector":"IT","description":"duplicate on purpose"}'
cat /tmp/t19.json; echo

echo "=== T-20: filter drives by status -> expect 200 (OPEN/CLOSED), 400 on garbage ==="
curl -s -o /tmp/t20a.json -w "HTTP %{http_code}\n" "$BASE/drives?status=OPEN"
cat /tmp/t20a.json; echo
curl -s -o /tmp/t20b.json -w "HTTP %{http_code}\n" "$BASE/drives?status=NOT_A_STATUS"
cat /tmp/t20b.json; echo

echo "=== T-21: ineligible via programme/grad-year/skills mismatch -> expect eligible:false ==="
curl -s -o /tmp/student.json -w "HTTP %{http_code}\n" -X POST "$BASE/students" \
  -H "Content-Type: application/json" \
  -d '{"id":"STU-VERIFY-1","name":"Verify Student","email":"verify.student@example.com","programme":"ECE","graduationYear":2024,"cgpa":9.0,"activeBacklogs":0,"skills":["Python"]}'

curl -s -o /tmp/drive.json -w "HTTP %{http_code}\n" -X POST "$BASE/drives" \
  -H "Content-Type: application/json" \
  -d "{\"companyId\":\"$COMPANY_ID\",\"role\":\"Java Backend\",\"location\":\"Remote\",\"packageAmount\":1200000,\"deadline\":\"2030-01-01\",\"requiredSkills\":[\"Java\",\"Spring Boot\"],\"minCgpa\":8.0,\"maxBacklogsAllowed\":0,\"eligibleProgrammes\":[\"CSE\"],\"eligibleGraduationYears\":[2026]}"
DRIVE_ID=$(python3 -c "import json;print(json.load(open('/tmp/drive.json'))['id'])" 2>/dev/null)
echo "created drive id: $DRIVE_ID"

curl -s -o /tmp/t21.json -w "HTTP %{http_code}\n" "$BASE/drives/$DRIVE_ID/eligibility/STU-VERIFY-1"
cat /tmp/t21.json; echo
echo "(expect reasons covering programme mismatch, graduation-year mismatch, AND missing 'Spring Boot' skill)"

echo "=== T-22: malformed JSON body -> expect 400 MALFORMED_REQUEST ==="
curl -s -o /tmp/t22.json -w "HTTP %{http_code}\n" -X POST "$BASE/companies" \
  -H "Content-Type: application/json" \
  -d '{"name": "Broken JSON" '
cat /tmp/t22.json; echo

echo "=== Done. Review the HTTP codes and bodies above against docs/test_checklist.md expected outcomes. ==="
