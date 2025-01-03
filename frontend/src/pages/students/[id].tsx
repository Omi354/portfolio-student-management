import {
  Box,
  Container,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from '@mui/material'
import { NextPage } from 'next'
import { useRouter } from 'next/router'
import useSWR from 'swr'
import { fetcher } from '@/utils'

type StudentCourseProps = {
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

const StudentDetail: NextPage = () => {
  const router = useRouter()
  const { id } = router.query
  const url = process.env.NEXT_PUBLIC_API_BASE_URL + '/students/' + id
  const { data, error, mutate } = useSWR(url, fetcher)

  if (error) return <div>An error has occurred.</div>
  if (!data) return <div>Loading...</div>

  return (
    <Box sx={{ backgroundColor: '#f9f9f9', minHeight: '100vh', py: 4 }}>
      <Container maxWidth="lg">
        <Typography variant="h4" gutterBottom>
          受講生詳細
        </Typography>
        <Typography variant="h5" gutterBottom>
          基本情報
        </Typography>
        <TableContainer component={Paper} sx={{ mb: 2 }}>
          <Table sx={{ minWidth: 650 }}>
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
              <TableRow key={data.student.id}>
                <TableCell>{data.student.fullName}</TableCell>
                <TableCell>{data.student.kana}</TableCell>
                <TableCell>{data.student.nickName}</TableCell>
                <TableCell>{data.student.email}</TableCell>
                <TableCell>{data.student.city}</TableCell>
                <TableCell>{data.student.age}</TableCell>
                <TableCell>{data.student.gender}</TableCell>
                <TableCell>{data.student.remark}</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </TableContainer>

        <Typography variant="h5" gutterBottom>
          受講コース
        </Typography>
        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650 }}>
            <TableHead>
              <TableRow>
                <TableCell>受講コース名</TableCell>
                <TableCell>受講開始日</TableCell>
                <TableCell>受講修了予定日</TableCell>
                <TableCell>申込状況</TableCell>
                <TableCell>申込状況更新日時</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {data.studentCourseList.map(
                (studentCourse: StudentCourseProps) => (
                  <TableRow key={studentCourse.id}>
                    <TableCell>{studentCourse.courseName}</TableCell>
                    <TableCell>{studentCourse.startDate}</TableCell>
                    <TableCell>{studentCourse.endDate}</TableCell>
                    <TableCell>
                      {studentCourse.enrollmentStatus.status}
                    </TableCell>
                    <TableCell>
                      {studentCourse.enrollmentStatus.createdAt}
                    </TableCell>
                  </TableRow>
                ),
              )}
            </TableBody>
          </Table>
        </TableContainer>
      </Container>
    </Box>
  )
}

export default StudentDetail
