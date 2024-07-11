import requests
import re
import os

# GitHub Token wird aus den Umgebungsvariablen abgerufen
GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")
ISSUE_NUMBER = os.getenv("ISSUE_NUMBER")
REPO_OWNER = os.getenv("GITHUB_REPOSITORY").split("/")[0]
REPO_NAME = os.getenv("GITHUB_REPOSITORY").split("/")[1]

headers = {
    "Authorization": f"token {GITHUB_TOKEN}",
    "Accept": "application/vnd.github.v3+json",
}


def get_issue(owner, repo, issue_number):
    url = f"https://api.github.com/repos/{owner}/{repo}/issues/{issue_number}"
    response = requests.get(url, headers=headers)
    response.raise_for_status()
    return response.json()


def extract_version(text):
    version_pattern = re.compile(r"Version of Stirling-PDF:\s*(\d+\.\d+\.\d+)")
    match = version_pattern.search(text)
    if match:
        return match.group(1)
    return None


# Issue-Details abrufen
issue = get_issue(REPO_OWNER, REPO_NAME, ISSUE_NUMBER)
issue_body = issue["body"]
print(issue_body)
issue_version = extract_version(issue_body)

# Ergebnis ausgeben
if issue_version:
    print(f"Gefundene Version im Issue: {issue_version}")
else:
    print("Keine Version im Issue gefunden.")
