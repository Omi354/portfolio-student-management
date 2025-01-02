import {
  Box,
  Button,
  Container,
  FormControl,
  Grid2,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
} from '@mui/material'
import axios, { AxiosError, AxiosResponse } from 'axios'
import type { NextPage } from 'next'
import { useEffect, useState } from 'react'
import { Controller, SubmitHandler, useForm } from 'react-hook-form'
import useSWR from 'swr'
import FilterInputs from '@/components/FilterInputs'
import StudentTable from '@/components/StudentTable'
import { fetcher } from '@/utils'

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
  const url = process.env.NEXT_PUBLIC_API_BASE_URL + '/students'
  const { data, error, mutate } = useSWR(url, fetcher)
  const [fullName, setFullName] = useState('')
  const [kana, setKana] = useState('')
  const [nickName, setNickName] = useState('')
  const [email, setEmail] = useState('')
  const [city, setCity] = useState('')
  const [maxAge, setMaxAge] = useState('')
  const [minAge, setMinAge] = useState('')
  const [gender, setGender] = useState('')
  const [remark, setRemark] = useState('')
  const [filteredData, setFilteredData] = useState<StudentDetailProps[]>([])
  const { control, handleSubmit, reset } = useForm<StudentDetailProps>({
    defaultValues: {
      student: {
        id: '',
        fullName: '',
        kana: '',
        nickName: '',
        email: '',
        city: '',
        age: undefined,
        gender: '',
      },
      studentCourseList: [
        {
          courseName: '',
          enrollmentStatus: {
            status: '',
          },
        },
      ],
    },
  })

  useEffect(() => {
    if (data) {
      let result = data

      if (fullName) {
        result = result.filter((studentData: StudentDetailProps) =>
          studentData.student.fullName.includes(fullName),
        )
      }
      if (kana) {
        result = result.filter((studentData: StudentDetailProps) =>
          studentData.student.kana.includes(kana),
        )
      }
      if (nickName) {
        result = result.filter((studentData: StudentDetailProps) =>
          studentData.student.nickName.includes(nickName),
        )
      }
      if (email) {
        result = result.filter((studentData: StudentDetailProps) =>
          studentData.student.email.includes(email),
        )
      }
      if (city) {
        result = result.filter((studentData: StudentDetailProps) =>
          studentData.student.city.includes(city),
        )
      }
      if (gender) {
        result = result.filter((studentData: StudentDetailProps) =>
          studentData.student.gender.includes(gender),
        )
      }
      if (remark) {
        result = result.filter((studentData: StudentDetailProps) =>
          studentData.student.remark.includes(remark),
        )
      }
      if (maxAge) {
        result = result.filter(
          (studentData: StudentDetailProps) =>
            studentData.student.age <= Number(maxAge),
        )
      }
      if (minAge) {
        result = result.filter(
          (studentData: StudentDetailProps) =>
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

  const onSubmit: SubmitHandler<StudentDetailProps> = (data) => {
    const url = process.env.NEXT_PUBLIC_API_BASE_URL + '/students'
    const headers = { 'Content-Type': 'application/json' }

    axios({ method: 'POST', url: url, data: data, headers: headers })
      .then((res: AxiosResponse) => {
        res.status === 200 && mutate()
        reset()
        alert(res.data.student.fullName + 'さんを登録しました')
      })
      .catch((err: AxiosError<{ error: string }>) => {
        console.log(err.message)
        alert(err.message)
      })
  }

  if (error) return <div>An error has occurred.</div>
  if (!data) return <div>Loading...</div>

  return (
    <Box sx={{ backgroundColor: '#f9f9f9', minHeight: '100vh', py: 4 }}>
      <Container maxWidth="lg">
        <Typography variant="h4" gutterBottom>
          受講生一覧
        </Typography>

        <Typography variant="h5" gutterBottom>
          新規受講生登録
        </Typography>

        <Grid2 container component="form" spacing={2}>
          <Grid2 size={6}>
            <Controller
              name="student.fullName"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  type="text"
                  label="氏名"
                  sx={{ backgroundColor: 'white', width: '100%' }}
                />
              )}
            />
          </Grid2>
          <Grid2 size={6}>
            <Controller
              name="student.kana"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  type="text"
                  label="カナ名"
                  sx={{ backgroundColor: 'white', width: '100%' }}
                />
              )}
            />
          </Grid2>
          <Grid2 size={6}>
            <Controller
              name="student.nickName"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  type="text"
                  label="ニックネーム"
                  sx={{ backgroundColor: 'white', width: '100%' }}
                />
              )}
            />
          </Grid2>

          <Grid2 size={6}>
            <Controller
              name="student.email"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  type="email"
                  label="メールアドレス"
                  sx={{ backgroundColor: 'white', width: '100%' }}
                />
              )}
            />
          </Grid2>
          <Grid2 size={6}>
            <Controller
              name="student.city"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  type="text"
                  label="居住地域"
                  sx={{ backgroundColor: 'white', width: '100%' }}
                />
              )}
            />
          </Grid2>

          <Grid2 size={6}>
            <Controller
              name="student.age"
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  type="number"
                  label="年齢"
                  sx={{ backgroundColor: 'white', width: '100%' }}
                />
              )}
            />
          </Grid2>
          <Grid2 size={4}>
            <Controller
              name="student.gender"
              control={control}
              render={({ field }) => (
                <FormControl sx={{ width: '100%' }}>
                  <InputLabel id="gender">性別</InputLabel>
                  <Select {...field} labelId="gender" label="gender">
                    <MenuItem value={'Male'}>Male</MenuItem>
                    <MenuItem value={'Female'}>Female</MenuItem>
                    <MenuItem value={'NON_BINARY'}>NON_BINARY</MenuItem>
                  </Select>
                </FormControl>
              )}
            />
          </Grid2>
          <Grid2 size={4}>
            <Controller
              name={`studentCourseList.${0}.courseName`}
              control={control}
              render={({ field }) => (
                <TextField
                  {...field}
                  type="text"
                  label="コース名"
                  sx={{ backgroundColor: 'white', width: '100%' }}
                />
              )}
            />
          </Grid2>
          <Grid2 size={4}>
            <Controller
              name={`studentCourseList.${0}.enrollmentStatus.status`}
              control={control}
              render={({ field }) => (
                <FormControl sx={{ width: '100%' }}>
                  <InputLabel id="status">申込状況</InputLabel>
                  <Select {...field} labelId="status" label="status">
                    <MenuItem value={'仮申込'}>仮申込</MenuItem>
                    <MenuItem value={'本申込'}>本申込</MenuItem>
                    <MenuItem value={'受講中'}>受講中</MenuItem>
                    <MenuItem value={'受講終了'}>受講終了</MenuItem>
                  </Select>
                </FormControl>
              )}
            />
          </Grid2>
          <Grid2 size={6}>
            <Button
              variant="contained"
              type="button"
              size="large"
              onClick={handleSubmit(onSubmit)}
              sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
            >
              登録
            </Button>
          </Grid2>
          <Grid2 size={6}>
            <Button
              variant="contained"
              type="button"
              color="error"
              size="large"
              onClick={handleSubmit(onSubmit)}
              sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
            >
              キャンセル
            </Button>
          </Grid2>
        </Grid2>

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
