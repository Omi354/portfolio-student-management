import {
  TableContainer,
  Paper,
  Table,
  TableHead,
  TableRow,
  TableCell,
  TableBody,
  Button,
} from '@mui/material'
import { StudentDetailProps } from '@/pages'

type StudentCourseTableProps = {
  data: StudentDetailProps
  onClick: (studentCourseId: string, enrollmentStatus: string) => void
}

type StudentCourseProps = {
  id: string
  courseName: string
  startDate: string
  endDate: string
  enrollmentStatus: {
    id: string
    status: string
    createdAt: string
  }
}

const StudentCourseTable: React.FC<StudentCourseTableProps> = ({
  data,
  onClick,
}) => {
  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }}>
        <TableHead>
          <TableRow>
            <TableCell>受講コース名</TableCell>
            <TableCell>受講開始日</TableCell>
            <TableCell>受講修了予定日</TableCell>
            <TableCell>申込状況</TableCell>
            <TableCell>申込状況更新日時</TableCell>
            <TableCell>申込状況更新</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {data.studentCourseList.map((studentCourse: StudentCourseProps) => (
            <TableRow key={studentCourse.id}>
              <TableCell>{studentCourse.courseName}</TableCell>
              <TableCell>{studentCourse.startDate}</TableCell>
              <TableCell>{studentCourse.endDate}</TableCell>
              <TableCell>{studentCourse.enrollmentStatus.status}</TableCell>
              <TableCell>{studentCourse.enrollmentStatus.createdAt}</TableCell>
              <TableCell>
                <Button
                  variant="outlined"
                  onClick={() =>
                    onClick(
                      studentCourse.id,
                      studentCourse.enrollmentStatus.status,
                    )
                  }
                >
                  申込状況更新
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default StudentCourseTable
