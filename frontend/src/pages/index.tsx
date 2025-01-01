import { Box, Container, Typography, Card, CardContent } from '@mui/material'
import type { NextPage } from 'next'
import useSWR from 'swr'
import { fetcher } from '@/utils'

type StudentProps = {
  student: {
    id: string
    fullName: string
    age: number
    email: string
    city: string
    gender: string
  }
  studentCourseList: {
    id: string
    courseName: string
    status: string
    startDate: string
    endDate: string
  }[]
}

const StudentPage: NextPage = () => {
  const url = 'http://localhost:8080/students'
  const { data, error } = useSWR(url, fetcher)

  if (error) return <div>An error has occurred.</div>
  if (!data) return <div>Loading...</div>

  const studentsData = data

  return (
    <Box sx={{ backgroundColor: '#f9f9f9', minHeight: '100vh', py: 4 }}>
      <Container maxWidth="md">
        <Typography variant="h4" gutterBottom>
          学生情報
        </Typography>

        {/* 学生の基本情報 */}
        {studentsData.map((studentData: StudentProps) => (
          // eslint-disable-next-line react/jsx-key
          <Card sx={{ mb: 4 }}>
            <CardContent>
              <Typography variant="h6">基本情報</Typography>
              <Typography>名前: {studentData.student.fullName}</Typography>
              <Typography>年齢: {studentData.student.age}</Typography>
              <Typography>メール: {studentData.student.email}</Typography>
              <Typography>住所: {studentData.student.city}</Typography>
              <Typography>性別: {studentData.student.gender}</Typography>
            </CardContent>
          </Card>
        ))}
      </Container>
    </Box>
  )
}

export default StudentPage
