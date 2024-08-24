import os
import argparse


def read_properties(file_path):
    with open(file_path, "r", encoding="utf-8") as file:
        return file.read().splitlines()


def check_difference(reference_file, file_list):
    reference_list = read_properties(reference_file)
    is_diff = False

    for file_path in file_list:
        if (
            file_path == reference_file
            or not file_path.endswith(".properties")
            or not os.path.basename(file_path).startswith("messages_")
        ):
            continue

        current_list = read_properties(file_path)
        reference_list_len = len(reference_list)
        current_list_len = len(current_list)

        if reference_list_len != current_list_len:
            print(f"Differenz in der Datei: {os.path.basename(file_path)}")
            is_diff = True
        if reference_list_len > current_list_len:
            print(
                f"Es fehlen Zeilen! Entweder fehlen Kommentare, leere Zeilen oder Übersetzungstrings! {reference_list_len}:{current_list_len}"
            )
        elif reference_list_len < current_list_len:
            print(
                f"Es gibt zuviele Zeilen! Überprüfen sie deine Übersetzungs Dateien! {reference_list_len}:{current_list_len}"
            )

    if is_diff:
        print("Check fail")
        print(reference_list)
        exit(1)
    print("Check success")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Find missing Keys")
    parser.add_argument(
        "--reference-file",
        required=True,
        help="Pfad zur Referenzdatei.",
    )
    parser.add_argument(
        "--files",
        type=str,
        required=True,
        help="Liste der geänderten Dateien, durch Leerzeichen getrennt.",
    )
    args = parser.parse_args()

    file_list = args.files.split()
    check_difference(args.reference_file, file_list)
