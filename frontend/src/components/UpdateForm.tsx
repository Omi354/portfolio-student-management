import {
  Grid2,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  FormHelperText,
  FormControlLabel,
  Checkbox,
  Button,
} from '@mui/material'

import { Controller, UseFormReturn } from 'react-hook-form'

import { StudentDetailProps } from '@/pages'

type updateFormProps = {
  studentData: StudentDetailProps

  updateForm: UseFormReturn<StudentDetailProps, any, undefined>
}

const UpdateForm: React.FC<updateFormProps> = ({ studentData, updateForm }) => {
  const control = updateForm.control

  return (
    <Grid2 container component="form" spacing={2}>
      <Grid2 size={6}>
        <Controller
          name="student.id"
          control={control}
          render={({ field, fieldState }) => (
            <TextField
              {...field}
              error={fieldState.invalid}
              helperText={fieldState.error?.message}
              type="text"
              label="id"
              sx={{ backgroundColor: 'white', width: '100%' }}
            />
          )}
        />
      </Grid2>

      <Grid2 size={6}>
        <Controller
          name="student.fullName"
          control={control}
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

      <Grid2 size={6}>
        <Controller
          name="student.remark"
          control={control}
          render={({ field, fieldState }) => (
            <TextField
              {...field}
              error={fieldState.invalid}
              helperText={fieldState.error?.message}
              type="text"
              label="備考"
              sx={{ backgroundColor: 'white', width: '100%' }}
            />
          )}
        />
      </Grid2>

      <Grid2 size={6}>
        <Controller
          name="student.isDeleted"
          control={control}
          render={({ field }) => (
            <FormControlLabel control={<Checkbox {...field} />} label="削除" />
          )}
        />
      </Grid2>

      <Grid2 size={4}>
        <Controller
          name={`studentCourseList.${0}.courseName`}
          control={control}
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
          name={`studentCourseList.${0}.startDate`}
          control={control}
          render={({ field, fieldState }) => (
            <TextField
              {...field}
              error={fieldState.invalid}
              helperText={fieldState.error?.message}
              type="string"
              label="受講開始日"
              sx={{ backgroundColor: 'white', width: '100%' }}
            />
          )}
        />
      </Grid2>

      <Grid2 size={4}>
        <Controller
          name={`studentCourseList.${0}.endDate`}
          control={control}
          render={({ field, fieldState }) => (
            <TextField
              {...field}
              error={fieldState.invalid}
              helperText={fieldState.error?.message}
              type="string"
              label="受講終了予定日"
              sx={{ backgroundColor: 'white', width: '100%' }}
            />
          )}
        />
      </Grid2>

      <Grid2 size={4}>
        <Controller
          name={`studentCourseList.${0}.enrollmentStatus.status`}
          control={control}
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
          size="large"
          sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
        >
          更新
        </Button>
      </Grid2>

      <Grid2 size={6}>
        <Button
          variant="contained"
          type="button"
          color="error"
          size="large"
          sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
        >
          キャンセル
        </Button>
      </Grid2>
    </Grid2>
  )
}

export default UpdateForm
