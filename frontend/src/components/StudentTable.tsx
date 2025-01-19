import {
  Box,
  Button,
  Paper,
  Table,
  TableBody,
  TableContainer,
  TableHead,
  TableRow,
} from '@mui/material'
import router from 'next/router'
import React from 'react'
import CustomTableCell from './CustomTableCell'
import { StudentDetailProps } from '@/types'

type StudentTableProps = {
  data: StudentDetailProps[]
  deleteStudent: (
    studentDetail: StudentDetailProps,
    e: React.MouseEvent<HTMLButtonElement, MouseEvent>,
  ) => void
}

const StudentTable = ({ data, deleteStudent }: StudentTableProps) => {
  return (
    <Box>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow>
              <CustomTableCell minWidth={100} maxWidth={120}>
                氏名
              </CustomTableCell>
              <CustomTableCell minWidth={100} maxWidth={120}>
                カナ名
              </CustomTableCell>
              <CustomTableCell minWidth={100} maxWidth={120}>
                ニックネーム
              </CustomTableCell>
              <CustomTableCell minWidth={100} maxWidth={120}>
                メールアドレス
              </CustomTableCell>
              <CustomTableCell minWidth={100} maxWidth={120}>
                居住地域
              </CustomTableCell>
              <CustomTableCell minWidth={100} maxWidth={120}>
                年齢
              </CustomTableCell>
              <CustomTableCell minWidth={100} maxWidth={120}>
                性別
              </CustomTableCell>
              <CustomTableCell minWidth={100} maxWidth={120}>
                備考
              </CustomTableCell>
              <CustomTableCell minWidth={100} maxWidth={120}>
                操作
              </CustomTableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((studentData) => (
              <TableRow
                key={studentData.student.id}
                onClick={() =>
                  router.push(`/students/${studentData.student.id}`)
                }
                style={{ cursor: 'pointer' }}
              >
                <CustomTableCell minWidth={100} maxWidth={200}>
                  {studentData.student.fullName}
                </CustomTableCell>
                <CustomTableCell minWidth={100} maxWidth={200}>
                  {studentData.student.kana}
                </CustomTableCell>
                <CustomTableCell minWidth={100} maxWidth={200}>
                  {studentData.student.nickName}
                </CustomTableCell>
                <CustomTableCell minWidth={100} maxWidth={400}>
                  {studentData.student.email}
                </CustomTableCell>
                <CustomTableCell minWidth={100} maxWidth={200}>
                  {studentData.student.city}
                </CustomTableCell>
                <CustomTableCell minWidth={100} maxWidth={200}>
                  {studentData.student.age}
                </CustomTableCell>
                <CustomTableCell minWidth={100} maxWidth={200}>
                  {studentData.student.gender}
                </CustomTableCell>
                <CustomTableCell minWidth={100} maxWidth={400}>
                  {studentData.student.remark}
                </CustomTableCell>
                <CustomTableCell minWidth={100} maxWidth={100}>
                  <Button
                    variant="contained"
                    color="error"
                    size="small"
                    onClick={(e) => deleteStudent(studentData, e)}
                  >
                    削除
                  </Button>
                </CustomTableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  )
}

export default StudentTable
