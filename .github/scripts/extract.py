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
    # Anpassung des regulären Ausdrucks, um verschiedene Trennzeichen zu unterstützen
    version_pattern = re.compile(
        r"### Version of Stirling-PDF\s*\n\s*([\d{1,2}[\.\,\s]\d{1,2}[\.\,\s]\d{2,4}]+)"
    )
    match = version_pattern.search(text)
    if match:
        return match.group(1)
    return None


def post_comment(owner, repo, issue_number, message):
    url = f"https://api.github.com/repos/{owner}/{repo}/issues/{issue_number}/comments"
    data = {"body": message}
    response = requests.post(url, json=data, headers=headers, timeout=60)
    response.raise_for_status()
    return response.json()


# Issue-Details abrufen
issue = get_issue(REPO_OWNER, REPO_NAME, ISSUE_NUMBER)
issue_body = issue["body"]
issue_version = extract_version(issue_body)

# Ergebnis ausgeben und Kommentar hinzufügen, wenn keine Version gefunden wurde
if issue_version:
    print(f"Gefundene Version im Issue: {issue_version}")
else:
    print("Keine Version im Issue gefunden.")
    message = "Es wurde keine gültige Version von Stirling-PDF im angegebenen Format gefunden. Bitte geben Sie die Version im Format `### Version of Stirling-PDF\nx.y.z` an."
    post_comment(REPO_OWNER, REPO_NAME, ISSUE_NUMBER, message)
