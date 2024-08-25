import os
import argparse


def read_properties(file_path):
    with open(file_path, "r", encoding="utf-8") as file:
        return file.read().splitlines()


def check_difference(reference_file, file_list, branch):
    print(
        f"Prüfung mit der Datei: {reference_file} - Überprüft wird der Branch: {branch}"
    )
    reference_list = read_properties(reference_file)
    is_diff = False

    for file_path in file_list:
        if (
            branch + "/" + file_path == reference_file
            or not file_path.endswith(".properties")
            or not os.path.basename(file_path).startswith("messages_")
        ):
            print(
                f"Datei '{os.path.basename(branch + "/" + file_path)}' wird ignoriert."
            )
            continue

        print(
            f"Überprüfung der Sprachdatei '{os.path.basename(branch + "/" + file_path)}'..."
        )

        current_list = read_properties(branch + "/" + file_path)
        reference_list_len = len(reference_list)
        current_list_len = len(current_list)

        if reference_list_len != current_list_len:
            print("Test 1 nicht bestanden!")
            print(
                f"Differenz in der Datei: {os.path.basename(branch + "/" + file_path)}"
            )
            is_diff = True
            if reference_list_len > current_list_len:
                print(
                    f"Es fehlen Zeilen! Entweder fehlen Kommentare, leere Zeilen oder Übersetzungstrings! {reference_list_len}:{current_list_len}"
                )
            elif reference_list_len < current_list_len:
                print(
                    f"Es gibt zuviele Zeilen! Überprüfen sie deine Übersetzungs Dateien! {reference_list_len}:{current_list_len}"
                )
        else:
            print("Test 1 bestanden")
            for item in current_list:
                print(item)


    if is_diff:
        print("Check fail")
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
        "--branch",
        type=str,
        required=True,
        help="",
    )
    parser.add_argument(
        "--files",
        nargs="+",
        required=True,
        help="Liste der geänderten Dateien, durch Leerzeichen getrennt.",
    )
    args = parser.parse_args()

    file_list = args.files  # .split()
    check_difference(args.reference_file, file_list, args.branch)
