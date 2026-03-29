class DicodingStudent {
  String fullName;
  int age;

  DicodingStudent(this.fullName, this.age);

  int incrementAge() {
    // TODO 1

    return age + 1;

    // End of TODO 1
  }

  Future<String> getStudentInfo() {
    // TODO 2

    return Future.delayed(
      Duration(seconds: 3),
          () => "Nama Lengkap: $fullName, Umur: $age tahun",
    );

    // End of TODO 2
  }
}

dynamic createStudent() {
  // TODO 3

  var dicodingStudent = DicodingStudent("Lutfi", 18);
  return dicodingStudent;

  // End of TODO 3
}