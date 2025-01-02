import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material'
import React from 'react'

type StudentTableProps = {
  data: {
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
  }[]
}

const StudentTable: React.FC<StudentTableProps> = ({ data }) => {
  return (
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
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default StudentTable
