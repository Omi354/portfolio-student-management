import {
  Grid2,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Button,
  Box,
  Typography,
} from '@mui/material'

import { Control, Controller } from 'react-hook-form'

import { StudentDetailProps } from '@/pages'

type updateFormProps = {
  studentData: StudentDetailProps
  control: Control<StudentDetailProps>
  onSubmit: () => void
  onClick: () => void
}

const UpdateForm: React.FC<updateFormProps> = ({
  studentData,
  control,
  onSubmit,
  onClick,
}) => {
  return (
    <Box sx={{ m: 2 }}>
      <form>
        <Typography variant="h6" gutterBottom>
          受講生
        </Typography>

        <Grid2 container spacing={2}>
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

          <Grid2 size={4}>
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

          <Grid2 size={4}>
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

          {studentData.student.gender !== '' && (
            <Grid2 size={4}>
              <FormControl sx={{ width: '100%' }}>
                <InputLabel id="gender">性別</InputLabel>
                <Controller
                  name="student.gender"
                  control={control}
                  render={({ field }) => (
                    <Select {...field} labelId="gender" label="gender">
                      <MenuItem value={'Male'}>Male</MenuItem>
                      <MenuItem value={'Female'}>Female</MenuItem>
                      <MenuItem value={'NON_BINARY'}>NON_BINARY</MenuItem>
                    </Select>
                  )}
                />
              </FormControl>
            </Grid2>
          )}

          <Grid2 size={12}>
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
        </Grid2>

        <Typography variant="h6" gutterBottom sx={{ mt: 3 }}>
          受講コース
        </Typography>

        {studentData.studentCourseList.map((studentCourse, index) => (
          <Grid2 container spacing={2} key={index}>
            <Grid2 size={4}>
              <Controller
                name={`studentCourseList.${index}.courseName`}
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

            <Grid2 size={3}>
              <Controller
                name={`studentCourseList.${index}.startDate`}
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

            <Grid2 size={3}>
              <Controller
                name={`studentCourseList.${index}.endDate`}
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

            <Grid2 size={2}>
              <FormControl sx={{ width: '100%' }}>
                <InputLabel id="status">申込状況</InputLabel>
                <Controller
                  name={`studentCourseList.${index}.enrollmentStatus.status`}
                  control={control}
                  render={({ field }) => (
                    <Select {...field} labelId="status" label="status">
                      <MenuItem value={'仮申込'}>仮申込</MenuItem>
                      <MenuItem value={'本申込'}>本申込</MenuItem>
                      <MenuItem value={'受講中'}>受講中</MenuItem>
                      <MenuItem value={'受講終了'}>受講終了</MenuItem>
                    </Select>
                  )}
                />
              </FormControl>
            </Grid2>
          </Grid2>
        ))}

        <Grid2 container spacing={2} sx={{ mt: 3 }}>
          <Grid2 size={6}>
            <Button
              variant="contained"
              type="button"
              size="large"
              onClick={onSubmit}
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
              onClick={onClick}
              sx={{ fontWeight: 'bold', color: 'white', width: '100%' }}
            >
              キャンセル
            </Button>
          </Grid2>
        </Grid2>
      </form>
    </Box>
  )
}

export default UpdateForm
