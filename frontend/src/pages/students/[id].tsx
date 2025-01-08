import {
  Box,
  Button,
  Container,
  Dialog,
  DialogTitle,
  Typography,
} from '@mui/material'
import axios, { AxiosError, AxiosResponse } from 'axios'
import { NextPage } from 'next'
import { useRouter } from 'next/router'
import { useEffect, useState } from 'react'
import { SubmitHandler, useForm } from 'react-hook-form'
import useSWR from 'swr'
import EditForm from '@/components/EditForm'
import EnrollmentStatusForm from '@/components/EnrollmentStatusForm'
import Loading from '@/components/Loading'
import StudentCourseTable from '@/components/StudentCourseTable'
import StudentInfoTable from '@/components/StudentInfoTable'
import { EnrollmentStatusFormData, StudentDetailProps } from '@/types'
import { fetcher } from '@/utils'

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
  const enrollmentStatusFormHandler = useForm<EnrollmentStatusFormData>()

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
        console.log(err.message)
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

  const updateEnrollmentStatus: SubmitHandler<EnrollmentStatusFormData> = (
    formData,
  ) => {
    const confirmDelete = window.confirm(
      'ステータスを元に戻すことはできません。誤りはありませんか？',
    )

    if (!confirmDelete) {
      return
    }
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
      .catch((err: AxiosError<{ message: string }>) => {
        console.log(err)
        alert(err.response?.data.message)
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
  if (!data) return <Loading />

  return (
    <Box sx={{ backgroundColor: '#faf6f2', minHeight: '100vh', py: 4 }}>
      <Container maxWidth="lg">
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            textAlign: 'center',
            alignItems: 'center',
          }}
        >
          <Typography variant="h4" gutterBottom>
            受講生詳細
          </Typography>
          <Box>
            <Button
              onClick={() => router.push('/students')}
              variant="contained"
              sx={{
                fontWeight: 'bold',
                color: 'white',
                minWidth: '80px',
                mr: 2,
              }}
              color="error"
            >
              一覧へ戻る
            </Button>
            <Button
              onClick={handleEditFormOpen}
              variant="contained"
              sx={{ fontWeight: 'bold', color: 'white', minWidth: '80px' }}
            >
              編集
            </Button>
          </Box>
        </Box>
        <Typography variant="h5" gutterBottom>
          基本情報
        </Typography>
        <StudentInfoTable data={data} />

        <Typography variant="h5" gutterBottom>
          受講コース
        </Typography>

        <StudentCourseTable data={data} onClick={handleStatusFormOpen} />

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
          <EnrollmentStatusForm
            formHandler={enrollmentStatusFormHandler}
            onSubmit={enrollmentStatusFormHandler.handleSubmit(
              updateEnrollmentStatus,
            )}
            onCancel={handleStatusFormClose}
          />
        </Dialog>
      </Container>
    </Box>
  )
}

export default StudentDetail
