import { Box, Container, Typography } from '@mui/material'
import type { NextPage } from 'next'
import { useEffect, useState } from 'react'
import useSWR from 'swr'
import FilterInputs from '@/components/FilterInputs'
import StudentTable from '@/components/StudentTable'
import { fetcher } from '@/utils'

export type StudentProps = {
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
  }
  studentCourseList: {
    id: string
    courseName: string
    startDate: string
    endDate: string
    enrollmentStatus: {
      id: string
      status: string
      createdAt: string
    }
  }[]
}

const StudentPage: NextPage = () => {
  const url = 'http://localhost:8080/students'
  const { data, error } = useSWR(url, fetcher)
  const [fullName, setFullName] = useState('')
  const [kana, setKana] = useState('')
  const [nickName, setNickName] = useState('')
  const [email, setEmail] = useState('')
  const [city, setCity] = useState('')
  const [maxAge, setMaxAge] = useState('')
  const [minAge, setMinAge] = useState('')
  const [gender, setGender] = useState('')
  const [remark, setRemark] = useState('')
  const [filteredData, setFilteredData] = useState<StudentProps[]>([])

  useEffect(() => {
    if (data) {
      let result = data

      if (fullName) {
        result = result.filter((studentData: StudentProps) =>
          studentData.student.fullName.includes(fullName),
        )
      }
      if (kana) {
        result = result.filter((studentData: StudentProps) =>
          studentData.student.kana.includes(kana),
        )
      }
      if (nickName) {
        result = result.filter((studentData: StudentProps) =>
          studentData.student.nickName.includes(nickName),
        )
      }
      if (email) {
        result = result.filter((studentData: StudentProps) =>
          studentData.student.email.includes(email),
        )
      }
      if (city) {
        result = result.filter((studentData: StudentProps) =>
          studentData.student.city.includes(city),
        )
      }
      if (gender) {
        result = result.filter((studentData: StudentProps) =>
          studentData.student.gender.includes(gender),
        )
      }
      if (remark) {
        result = result.filter((studentData: StudentProps) =>
          studentData.student.remark.includes(remark),
        )
      }
      if (maxAge) {
        result = result.filter(
          (studentData: StudentProps) =>
            studentData.student.age <= Number(maxAge),
        )
      }
      if (minAge) {
        result = result.filter(
          (studentData: StudentProps) =>
            studentData.student.age >= Number(minAge),
        )
      }
      setFilteredData(result)
    }
  }, [
    data,
    fullName,
    kana,
    nickName,
    email,
    city,
    maxAge,
    minAge,
    gender,
    remark,
  ])

  if (error) return <div>An error has occurred.</div>
  if (!data) return <div>Loading...</div>

  return (
    <Box sx={{ backgroundColor: '#f9f9f9', minHeight: '100vh', py: 4 }}>
      <Container maxWidth="lg">
        <Typography variant="h4" gutterBottom>
          受講生一覧
        </Typography>

        <FilterInputs
          fullName={fullName}
          setFullName={setFullName}
          kana={kana}
          setKana={setKana}
          nickName={nickName}
          setNickName={setNickName}
          email={email}
          setEmail={setEmail}
          city={city}
          setCity={setCity}
          maxAge={maxAge}
          setMaxAge={setMaxAge}
          minAge={minAge}
          setMinAge={setMinAge}
          gender={gender}
          setGender={setGender}
          remark={remark}
          setRemark={setRemark}
        />

        <StudentTable data={filteredData} />
      </Container>
    </Box>
  )
}

export default StudentPage
