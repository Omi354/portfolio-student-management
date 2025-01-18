import {
  Box,
  Button,
  Container,
  Dialog,
  DialogTitle,
  Typography,
} from '@mui/material'
import axios, { AxiosError, AxiosResponse } from 'axios'
import type { NextPage } from 'next'
import { useEffect, useState } from 'react'
import { SubmitHandler, useForm } from 'react-hook-form'
import useSWR from 'swr'
import Error from '@/components/Error'
import FilterInputs from '@/components/FilterInputs'
import Loading from '@/components/Loading'
import RegisterForm from '@/components/RegisterForm'
import StudentTable from '@/components/StudentTable'
import { StudentDetailProps } from '@/types'
import { fetcher } from '@/utils'

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
  const [open, setOpen] = useState(false)
  const { control, handleSubmit, reset } = useForm<StudentDetailProps>({
    defaultValues: {
      student: {
        id: '',
        fullName: '',
        kana: '',
        nickName: '',
        email: '',
        city: '',
        age: 0,
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

  const registerStudent: SubmitHandler<StudentDetailProps> = (formData) => {
    const url = process.env.NEXT_PUBLIC_API_BASE_URL + '/students'
    const headers = { 'Content-Type': 'application/json' }

    axios({ method: 'POST', url: url, data: formData, headers: headers })
      .then((res: AxiosResponse) => {
        res.status === 200 && mutate()
        handleClickClose()
        alert(res.data.student.fullName + 'さんを登録しました')
      })
      .catch((err: AxiosError<{ error: string }>) => {
        console.log(err.message)
        alert(err.message)
      })
  }

  const handleClickOpen = () => {
    setOpen(true)
  }

  const handleClickClose = () => {
    setOpen(false)
    reset()
  }

  const deleteStudent = (
    studentData: StudentDetailProps,
    e: React.MouseEvent<HTMLButtonElement, MouseEvent>,
  ) => {
    e.stopPropagation()

    const confirmDelete = window.confirm(
      studentData.student.fullName + 'さんを本当に削除してよろしいですか？',
    )

    if (!confirmDelete) {
      return
    }

    const studentId = studentData.student.id
    const modifiedData = studentData
    modifiedData.student.isDeleted = true

    const url = process.env.NEXT_PUBLIC_API_BASE_URL + '/students/' + studentId
    const headers = { 'Content-Type': 'application/json' }
    console.log(url)

    axios({ method: 'PATCH', url: url, data: modifiedData, headers: headers })
      .then((res: AxiosResponse) => {
        res.status === 200 && mutate()
        alert(
          modifiedData.student.fullName +
            'さんを削除しました\n\n' +
            'データの復旧を希望の場合は管理者にお問い合わせください',
        )
      })
      .catch((err: AxiosError<{ error: string }>) => {
        console.log(err)
        alert(err.message)
      })
  }

  if (error) return <Error error={error} />
  if (!data) return <Loading />

  return (
    <Box sx={{ backgroundColor: '#faf6f2', minHeight: '100vh', py: 4 }}>
      <Container maxWidth="lg">
        <Box
          mb={3}
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
          }}
        >
          <Typography variant="h4">受講生一覧</Typography>
          <Button
            variant="contained"
            onClick={handleClickOpen}
            size="large"
            sx={{ fontWeight: 'bold', color: 'white' }}
          >
            新規登録
          </Button>
        </Box>

        <Dialog open={open} onClose={handleClickClose}>
          <DialogTitle>新規受講生登録</DialogTitle>
          <RegisterForm
            control={control}
            onSubmit={handleSubmit(registerStudent)}
            onClick={handleClickClose}
          />
        </Dialog>

        <Box mb={3}>
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
        </Box>

        <StudentTable data={filteredData} deleteStudent={deleteStudent} />
      </Container>
    </Box>
  )
}

export default StudentPage
