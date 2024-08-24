"""check_language_files.py"""

import os
import glob
import argparse


def read_properties(file_path):
    with open(file_path, "r", encoding="utf-8") as file:
        return file.read().splitlines()


def check_defference(reference_file, file_list):
    reference_list = read_properties(reference_file)
    isDiff = False

    # for file_path in glob.glob(os.path.join(directory, "messages_*.properties")):
    for file_path in file_list:
        if (
            file_path == reference_file
            or file_path.endswith("GB.properties")
            or not file_path.endswith(".properties")
            or not os.path.basename(file_path).startswith("messages_")
        ):
            continue
        current_list = read_properties(file_path)
        reference_list_len = len(reference_list)
        current_list_len = len(current_list)

        if reference_list_len != current_list_len:
            print(f"Differenz in der Datei: {os.path.basename(file_path)}")
            isDiff = True
        if reference_list_len > current_list_len:
            print(
                f"Es fehlen Zeilen! Entweder fehlen Kommentare, leere Zeilen oder Übersetzungstrings! {reference_list_len}:{current_list_len}"
            )
        elif reference_list_len < current_list_len:
            print(
                f"Es gibt zuviele Zeilen! Überprüfen sie deine Übersetzungs Dateien! {reference_list_len}:{current_list_len}"
            )
    if isDiff:
        print("Check fail")
        print(reference_list)
        exit(1)
    print("Check success")

def list_of_strings(arg):
    return arg.split(',')

if __name__ == "__main__":
    directory = os.getcwd() + "/src/main/resources"
    parser = argparse.ArgumentParser(description="Find missing Keys")
    parser.add_argument(
        "--reference-file",
        default=os.path.join(directory, "messages_en_GB.properties"),
        help="Pfad zur Referenzdatei aus dem main-Branch.",
    )
    parser.add_argument(
        "--files",
        type=list_of_strings,
        required=True,
        help="List of changed files separated by spaces",
    )
    args = parser.parse_args()

    # Split the files into a list
    file_list = args.files.split()

    check_defference(args.reference_file, file_list)
