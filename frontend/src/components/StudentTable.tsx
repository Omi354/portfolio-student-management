import {
  Box,
  Button,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material'
import React from 'react'
import { UseFormReturn } from 'react-hook-form'
import { StudentDetailProps } from '@/pages'

type StudentTableProps = {
  data: StudentDetailProps[]
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  updateForm: UseFormReturn<StudentDetailProps, any, undefined>
  deleteStudent: (studentDetail: StudentDetailProps) => void
}

const StudentTable: React.FC<StudentTableProps> = ({ data, deleteStudent }) => {
  return (
    <Box>
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
              <TableCell>操作</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((studentData) => (
              <TableRow key={studentData.student.id}>
                <TableCell>{studentData.student.fullName}</TableCell>
                <TableCell>{studentData.student.kana}</TableCell>
                <TableCell>{studentData.student.nickName}</TableCell>
                <TableCell>{studentData.student.email}</TableCell>
                <TableCell>{studentData.student.city}</TableCell>
                <TableCell>{studentData.student.age}</TableCell>
                <TableCell>{studentData.student.gender}</TableCell>
                <TableCell>{studentData.student.remark}</TableCell>
                <TableCell>
                  <Button
                    variant="contained"
                    color="error"
                    size="small"
                    onClick={() => deleteStudent(studentData)}
                  >
                    削除
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  )
}

export default StudentTable
