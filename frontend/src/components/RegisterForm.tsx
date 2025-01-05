import {
  Box,
  Button,
  FormControl,
  FormHelperText,
  Grid2,
  InputLabel,
  MenuItem,
  Select,
  TextField,
} from '@mui/material'

import { Control, Controller } from 'react-hook-form'
import { StudentDetailProps } from '@/types'
import { validationRules } from '@/utils'

type RegisterFormProps = {
  control: Control<StudentDetailProps>
  onSubmit: () => void
  onClick: () => void
}

const RegisterForm: React.FC<RegisterFormProps> = ({
  control,
  onSubmit,
  onClick,
}) => {
  return (
    <Box sx={{ m: 2 }}>
      <Grid2 container component="form" spacing={2}>
        <Grid2 size={6}>
          <Controller
            name="student.fullName"
            control={control}
            rules={validationRules.student.fullName}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="text"
                label="氏名"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Controller
            name="student.kana"
            control={control}
            rules={validationRules.student.kana}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="text"
                label="カナ名"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Controller
            name="student.nickName"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                type="text"
                label="ニックネーム"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Controller
            name="student.email"
            control={control}
            rules={validationRules.student.email}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="email"
                label="メールアドレス"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Controller
            name="student.city"
            control={control}
            rules={validationRules.student.city}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="text"
                label="居住地域"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Controller
            name="student.age"
            control={control}
            rules={validationRules.student.age}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="number"
                label="年齢"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={4}>
          <Controller
            name="student.gender"
            control={control}
            rules={validationRules.student.gender}
            render={({ field, fieldState }) => (
              <FormControl sx={{ width: '100%' }} error={fieldState.invalid}>
                <InputLabel id="gender">性別</InputLabel>

                <Select {...field} labelId="gender" label="gender">
                  <MenuItem value={'Male'}>Male</MenuItem>

                  <MenuItem value={'Female'}>Female</MenuItem>

                  <MenuItem value={'NON_BINARY'}>NON_BINARY</MenuItem>
                </Select>
                <FormHelperText>{fieldState.error?.message}</FormHelperText>
              </FormControl>
            )}
          />
        </Grid2>

        <Grid2 size={4}>
          <Controller
            name={`studentCourseList.${0}.courseName`}
            control={control}
            rules={validationRules.studentCourseList.courseName}
            render={({ field, fieldState }) => (
              <TextField
                {...field}
                error={fieldState.invalid}
                helperText={fieldState.error?.message}
                type="text"
                label="コース名"
                sx={{ backgroundColor: 'white', width: '100%' }}
              />
            )}
          />
        </Grid2>

        <Grid2 size={4}>
          <Controller
            name={`studentCourseList.${0}.enrollmentStatus.status`}
            control={control}
            rules={validationRules.studentCourseList.enrollmentStatus.status}
            render={({ field, fieldState }) => (
              <FormControl sx={{ width: '100%' }} error={fieldState.invalid}>
                <InputLabel id="status">申込状況</InputLabel>

                <Select {...field} labelId="status" label="status">
                  <MenuItem value={'仮申込'}>仮申込</MenuItem>
                  <MenuItem value={'本申込'}>本申込</MenuItem>
                  <MenuItem value={'受講中'}>受講中</MenuItem>
                  <MenuItem value={'受講終了'}>受講終了</MenuItem>
                </Select>
                <FormHelperText>{fieldState.error?.message}</FormHelperText>
              </FormControl>
            )}
          />
        </Grid2>

        <Grid2 size={6}>
          <Button
            variant="contained"
            type="button"
            color="error"
            size="large"
            onClick={onClick}
            sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
          >
            キャンセル
          </Button>
        </Grid2>
        <Grid2 size={6}>
          <Button
            variant="contained"
            type="button"
            size="large"
            onClick={onSubmit}
            sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
          >
            登録
          </Button>
        </Grid2>
      </Grid2>
    </Box>
  )
}

export default RegisterForm
