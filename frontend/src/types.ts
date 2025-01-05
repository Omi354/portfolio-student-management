export type StudentDetailProps = {
  student: {
    id: string
    fullName: string
    kana: string
    nickName: string
    email: string
    city: string
    age: number
    gender: string
    remark: string
    isDeleted: boolean
  }
  studentCourseList: {
    id: string
    studentId: string
    courseName: string
    startDate: string
    endDate: string
    enrollmentStatus: {
      id: string
      studentCourseId: string
      status: string
      createdAt: string
    }
  }[]
}

export type StudentCourseProps = {
  id: string
  courseName: string
  startDate: string
  endDate: string
  enrollmentStatus: {
    id: string
    status: string
    createdAt: string
  }
}

export type EnrollmentStatusFormData = {
  studentCourseId: string
  status: string
}
