import {
  Box,
  Button,
  Checkbox,
  FormControl,
  FormControlLabel,
  FormHelperText,
  Grid2,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TextField,
} from '@mui/material'
import React from 'react'
import { Controller, useForm, UseFormReturn } from 'react-hook-form'
import { StudentDetailProps } from '@/pages'
import { Check, CheckBox } from '@mui/icons-material';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import UpdateForm from './UpdateForm';

type StudentTableProps = {
  data: StudentDetailProps[];
  updateForm: UseFormReturn<StudentDetailProps, any, undefined>
  deleteStudent: (studentDetail: StudentDetailProps) => void;
};

const StudentTable: React.FC<StudentTableProps> = ({ data, deleteStudent, updateForm }) => {

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
                  <Button variant="contained" color='error' size="small" onClick={() => deleteStudent(studentData)}>
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
