import {
  Box,
  Button,
  Container,
  Dialog,
  DialogTitle,
  FormControl,
  Grid2,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
} from '@mui/material'
import axios, { AxiosError, AxiosResponse } from 'axios'
import { NextPage } from 'next'
import { useRouter } from 'next/router'
import { useEffect, useState } from 'react'
import { Controller, SubmitHandler, useForm } from 'react-hook-form'
import useSWR from 'swr'
import EditForm from '@/components/EditForm'
import StudentCourseTable from '@/components/StudentCourseTable'
import StudentInfoTable from '@/components/StudentInfoTable'
import { StudentDetailProps } from '@/pages/index'
import { fetcher } from '@/utils'

export type EnrollmentStatusFormProps = {
  studentCourseId: string
  status: string
}

const StudentDetail: NextPage = () => {
  const router = useRouter()
  const { id } = router.query
  const url = process.env.NEXT_PUBLIC_API_BASE_URL + '/students/'
  const [isEditFormOpen, setIsEditFormOpen] = useState(false)
  const [isStatusFormOpen, setIsStatusFormOpen] = useState(false)
  const { data, error, mutate } = useSWR(id ? url + id : null, fetcher)
  const editFormHandler = useForm<StudentDetailProps>({
    defaultValues: data,
  })
  const enrollmentStatusFormHandler = useForm<EnrollmentStatusFormProps>()

  useEffect(() => {
    if (data) {
      editFormHandler.reset(data)
    }
  }, [data, editFormHandler])

  const updateStudent: SubmitHandler<StudentDetailProps> = (formData) => {
    const url = process.env.NEXT_PUBLIC_API_BASE_URL + '/students'
    const headers = { 'Content-Type': 'application/json' }

    axios({ method: 'PUT', url: url, data: formData, headers: headers })
      .then((res: AxiosResponse) => {
        res.status === 200 && mutate()
        alert(formData.student.fullName + 'さんの情報を更新しました')
        handleEditFormClose()
      })
      .catch((err: AxiosError<{ error: string }>) => {
        console.log(err)
        alert(err.message)
      })
  }

  const handleEditFormOpen = () => {
    setIsEditFormOpen(true)
    editFormHandler.reset()
  }
  const handleEditFormClose = () => {
    setIsEditFormOpen(false)
    editFormHandler.reset()
  }
  const handleEditFormReset = () => {
    editFormHandler.reset()
  }

  const updateEnrollmentStatus: SubmitHandler<EnrollmentStatusFormProps> = (
    formData,
  ) => {
    const url =
      process.env.NEXT_PUBLIC_API_BASE_URL +
      '/students/courses/enrollment-status'
    const headers = { 'Content-Type': 'application/json' }

    axios({ method: 'post', url: url, data: formData, headers: headers })
      .then((res: AxiosResponse) => {
        res.status === 200 && mutate()
        alert('申込状況を更新しました')
        handleStatusFormClose()
      })
      .catch((err: AxiosError<{ error: string }>) => {
        console.log(err.message)
        alert(err.message)
      })
  }

  const handleStatusFormOpen = (
    studentCourseId: string,
    enrollmentStatus: string,
  ) => {
    setIsStatusFormOpen(true)
    enrollmentStatusFormHandler.setValue('studentCourseId', studentCourseId)
    enrollmentStatusFormHandler.setValue('status', enrollmentStatus)
  }
  const handleStatusFormClose = () => {
    setIsStatusFormOpen(false)
  }

  if (error) return <div>An error has occurred.</div>
  if (!data) return <div>Loading...</div>

  return (
    <Box sx={{ backgroundColor: '#f9f9f9', minHeight: '100vh', py: 4 }}>
      <Container maxWidth="lg">
        <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
          <Typography variant="h4" gutterBottom>
            受講生詳細
          </Typography>
          <Button onClick={handleEditFormOpen} variant="contained">
            編集
          </Button>
        </Box>
        <Typography variant="h5" gutterBottom>
          基本情報
        </Typography>
        <StudentInfoTable data={data} />

        <Typography variant="h5" gutterBottom>
          受講コース
        </Typography>

        <StudentCourseTable
          data={data}
          onClick={handleStatusFormOpen}
          formHandler={enrollmentStatusFormHandler}
        />

        <Dialog open={isEditFormOpen} onClose={handleEditFormClose}>
          <DialogTitle>受講生情報編集</DialogTitle>
          <EditForm
            studentData={data}
            control={editFormHandler.control}
            onSubmit={editFormHandler.handleSubmit(updateStudent)}
            onCancel={handleEditFormClose}
            onReset={handleEditFormReset}
          />
        </Dialog>

        <Dialog open={isStatusFormOpen} onClose={handleStatusFormClose}>
          <DialogTitle>申込状況更新</DialogTitle>
          <Box sx={{ m: 2 }}>
            <Grid2 container component="form" spacing={2}>
              <Grid2 size={12}>
                <Controller
                  name="studentCourseId"
                  control={enrollmentStatusFormHandler.control}
                  render={({ field, fieldState }) => (
                    <TextField
                      {...field}
                      error={fieldState.invalid}
                      helperText={fieldState.error?.message}
                      type="text"
                      label="コースID"
                      sx={{ backgroundColor: 'white', width: '100%' }}
                    />
                  )}
                />
              </Grid2>
              <Grid2 size={12}>
                <FormControl sx={{ width: '100%' }}>
                  <InputLabel id="status">申込状況</InputLabel>
                  <Controller
                    name="status"
                    control={enrollmentStatusFormHandler.control}
                    render={({ field }) => (
                      <Select {...field} labelId="status" label="status">
                        <MenuItem value={'仮申込'}>仮申込</MenuItem>
                        <MenuItem value={'本申込'}>本申込</MenuItem>
                        <MenuItem value={'受講中'}>受講中</MenuItem>
                        <MenuItem value={'受講終了…'}>受講終了</MenuItem>
                      </Select>
                    )}
                  />
                </FormControl>
              </Grid2>
              <Grid2 size={6}>
                <Button
                  variant="contained"
                  type="button"
                  size="large"
                  onClick={enrollmentStatusFormHandler.handleSubmit(
                    updateEnrollmentStatus,
                  )}
                  sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
                >
                  更新
                </Button>
              </Grid2>
              <Grid2 size={6}>
                <Button
                  variant="contained"
                  type="button"
                  color="error"
                  size="large"
                  onClick={handleStatusFormClose}
                  sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
                >
                  キャンセル
                </Button>
              </Grid2>
            </Grid2>
          </Box>
        </Dialog>
      </Container>
    </Box>
  )
}

export default StudentDetail
