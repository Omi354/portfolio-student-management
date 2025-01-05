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
import StudentCourseTable from '@/components/StudentCourseTable'
import StudentInfoTable from '@/components/StudentInfoTable'
import { StudentDetailProps } from '@/pages/index'
import { fetcher } from '@/utils'

const StudentDetail: NextPage = () => {
  const router = useRouter()
  const { id } = router.query
  const url = process.env.NEXT_PUBLIC_API_BASE_URL + '/students/'
  const [isEditFormOpen, setIsEditFormOpen] = useState(false)

  const { data, error, mutate } = useSWR(id ? url + id : null, fetcher)
  const { control, handleSubmit, reset } = useForm<StudentDetailProps>({
    defaultValues: data,
  })

  useEffect(() => {
    if (data) {
      reset(data)
    }
  }, [data, reset])

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
    reset()
  }

  const handleEditFormClose = () => {
    setIsEditFormOpen(false)
    reset()
  }

  const handleReset = () => {
    reset()
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

        <StudentCourseTable data={data} />

        <Dialog open={isEditFormOpen} onClose={handleEditFormClose}>
          <DialogTitle>受講生情報編集</DialogTitle>
          <EditForm
            studentData={data}
            control={control}
            onSubmit={handleSubmit(updateStudent)}
            onCancel={handleEditFormClose}
            onReset={handleReset}
          />
        </Dialog>
      </Container>
    </Box>
  )
}

export default StudentDetail
