import {
  TableContainer,
  Paper,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
} from '@mui/material'
import CustomTableCell from './CustomTableCell'
import { StudentDetailProps } from '@/types'

type StudentInfoTableProps = {
  data: StudentDetailProps
}

const StudentInfoTable = ({ data }: StudentInfoTableProps) => {
  return (
    <TableContainer component={Paper} sx={{ mb: 2 }}>
      <Table sx={{ minWidth: 650 }}>
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
          </TableRow>
        </TableHead>

        <TableBody>
          <TableRow key={data.student.id}>
            <TableCell
              sx={{
                minWidth: '100px',
                maxWidth: '400px',
              }}
            >
              {data.student.fullName}
            </TableCell>
            <TableCell
              sx={{
                minWidth: '100px',
                maxWidth: '400px',
              }}
            >
              {data.student.kana}
            </TableCell>
            <TableCell
              sx={{
                minWidth: '100px',
                maxWidth: '400px',
              }}
            >
              {data.student.nickName}
            </TableCell>
            <TableCell
              sx={{
                minWidth: '100px',
                maxWidth: '400px',
              }}
            >
              {data.student.email}
            </TableCell>
            <TableCell
              sx={{
                minWidth: '100px',
                maxWidth: '400px',
              }}
            >
              {data.student.city}
            </TableCell>
            <TableCell
              sx={{
                minWidth: '100px',
                maxWidth: '400px',
              }}
            >
              {data.student.age}
            </TableCell>
            <TableCell
              sx={{
                minWidth: '100px',
                maxWidth: '400px',
              }}
            >
              {data.student.gender}
            </TableCell>
            <TableCell
              sx={{
                minWidth: '100px',
                maxWidth: '400px',
              }}
            >
              {data.student.remark}
            </TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default StudentInfoTable
