import {
  TableContainer,
  Paper,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
} from '@mui/material'
import { StudentDetailProps } from '@/pages'

type StudentInfoTableProps = {
  data: StudentDetailProps
}

const StudentInfoTable: React.FC<StudentInfoTableProps> = ({ data }) => {
  return (
    <TableContainer component={Paper} sx={{ mb: 2 }}>
      <Table sx={{ minWidth: 650 }}>
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
          <TableRow key={data.student.id}>
            <TableCell>{data.student.fullName}</TableCell>
            <TableCell>{data.student.kana}</TableCell>
            <TableCell>{data.student.nickName}</TableCell>
            <TableCell>{data.student.email}</TableCell>
            <TableCell>{data.student.city}</TableCell>
            <TableCell>{data.student.age}</TableCell>
            <TableCell>{data.student.gender}</TableCell>
            <TableCell>{data.student.remark}</TableCell>
          </TableRow>
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default StudentInfoTable
