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
  Stack,
  TextField,
  Button,
} from '@mui/material'
import type { NextPage } from 'next'
import { useEffect, useState } from 'react'
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
  const [fullName, setFullName] = useState('')
  const [kana, setKana] = useState('')
  const [nickName, setNickName] = useState('')
  const [email, setEmail] = useState('')
  const [city, setCity] = useState('')
  const [maxAge, setMaxAge] = useState('')
  const [minAge, setMinAge] = useState('')
  const [gender, setGender] = useState('')
  const [remark, setRemark] = useState('')
  const [fillteredData, setFilteredData] = useState<StudentProps[]>([])

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

  const handleInputChange =
    (setter: React.Dispatch<React.SetStateAction<string>>) =>
    (event: React.ChangeEvent<HTMLInputElement>) => {
      setter(event.target.value)
    }

  const handleReset = () => {
    setFullName('')
    setKana('')
    setNickName('')
    setEmail('')
    setCity('')
    setMaxAge('')
    setMinAge('')
    setGender('')
    setRemark('')
  }

  return (
    <Box sx={{ backgroundColor: '#f9f9f9', minHeight: '100vh', py: 4 }}>
      <Container maxWidth="lg">
        <Typography variant="h4" gutterBottom>
          受講生一覧
        </Typography>

        <Stack direction="row" spacing={2}>
          <TextField
            label="氏名"
            value={fullName}
            variant="outlined"
            onChange={handleInputChange(setFullName)}
          />
          <TextField
            label="カナ名"
            value={kana}
            variant="outlined"
            onChange={handleInputChange(setKana)}
          />
          <TextField
            label="ニックネーム"
            value={nickName}
            variant="outlined"
            onChange={handleInputChange(setNickName)}
          />
          <TextField
            label="メールアドレス"
            value={email}
            variant="outlined"
            onChange={handleInputChange(setEmail)}
          />
          <TextField
            label="居住地域"
            value={city}
            variant="outlined"
            onChange={handleInputChange(setCity)}
          />
        </Stack>

        <Stack direction="row" spacing={2} sx={{ mt: 1, mb: 1 }}>
          <TextField
            label="最大年齢"
            value={maxAge}
            variant="outlined"
            onChange={handleInputChange(setMaxAge)}
          />
          <TextField
            label="最小年齢"
            value={minAge}
            variant="outlined"
            onChange={handleInputChange(setMinAge)}
          />
          <TextField
            label="性別"
            value={gender}
            variant="outlined"
            onChange={handleInputChange(setGender)}
          />
          <TextField
            label="備考"
            value={remark}
            variant="outlined"
            onChange={handleInputChange(setRemark)}
          />
          <Button variant="contained" onClick={handleReset}>
            リセット
          </Button>
        </Stack>

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
              {fillteredData.map((studentData: StudentProps) => (
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
