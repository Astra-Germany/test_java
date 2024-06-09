import os
from github import Github
import pandas as pd
import matplotlib.pyplot as plt

# Setup
token = os.getenv('GITHUB_TOKEN')
g = Github(token)
repo = g.get_repo("Ludy87/test_java")

# Gather issues
issues = repo.get_issues(state='all')
data = []

for issue in issues:
    if issue.pull_request is None:  # Exclude pull requests
        version = None
        browsers = None

        for label in issue.labels:
            if label.name.startswith('version:'):
                version = label.name.split('version:')[-1]
            if label.name.startswith('browser:'):
                browsers = label.name.split('browser:')[-1]

        data.append({
            'number': issue.number,
            'title': issue.title,
            'state': issue.state,
            'created_at': issue.created_at,
            'closed_at': issue.closed_at,
            'version': version,
            'browsers': browsers
        })

# Create DataFrame
df = pd.DataFrame(data)

# Basic statistics
version_stats = df['version'].value_counts()
browser_stats = df['browsers'].value_counts()

# Plot statistics
plt.figure(figsize=(10, 5))

plt.subplot(1, 2, 1)
version_stats.plot(kind='bar')
plt.title('Issues by Version')
plt.xlabel('Version')
plt.ylabel('Number of Issues')

plt.subplot(1, 2, 2)
browser_stats.plot(kind='bar')
plt.title('Issues by Browser')
plt.xlabel('Browser')
plt.ylabel('Number of Issues')

plt.tight_layout()
plt.savefig('issue_statistics.png')

# Save statistics to a CSV file
df.to_csv('issue_statistics.csv', index=False)

print("Statistics gathered and saved.")

# Optionally, you can upload the results back to the repo or post them as a comment on a specific issue or PR.
