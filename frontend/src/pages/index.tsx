import {
  Box,
  Container,
  Typography,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material'
import type { NextPage } from 'next'
import useSWR from 'swr'
import { fetcher } from '@/utils'

type StudentProps = {
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

  if (error) return <div>An error has occurred.</div>
  if (!data) return <div>Loading...</div>

  const studentsData = data

  return (
    <Box sx={{ backgroundColor: '#f9f9f9', minHeight: '100vh', py: 4 }}>
      <Container maxWidth="lg">
        <Typography variant="h4" gutterBottom>
          受講生一覧
        </Typography>

        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650 }} aria-label="simple table">
            <TableHead>
              <TableRow>
                <TableCell>氏名</TableCell>
                <TableCell>カナ名</TableCell>
                <TableCell>ニックネーム</TableCell>
                <TableCell>メールアドレス</TableCell>
                <TableCell>居住地域</TableCell>
                <TableCell>年齢</TableCell>
                <TableCell>性別</TableCell>
                <TableCell>備考</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {studentsData.map((studentData: StudentProps) => (
                <TableRow
                  key={studentData.student.id}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                >
                  <TableCell>{studentData.student.fullName}</TableCell>
                  <TableCell>{studentData.student.kana}</TableCell>
                  <TableCell>{studentData.student.nickName}</TableCell>
                  <TableCell>{studentData.student.email}</TableCell>
                  <TableCell>{studentData.student.city}</TableCell>
                  <TableCell>{studentData.student.age}</TableCell>
                  <TableCell>{studentData.student.gender}</TableCell>
                  <TableCell>{studentData.student.remark}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Container>
    </Box>
  )
}

export default StudentPage
