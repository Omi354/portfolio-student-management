import {
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  SelectChangeEvent,
  Stack,
  TextField,
} from '@mui/material'
import React from 'react'

type FilterInputsProps = {
  fullName: string
  setFullName: React.Dispatch<React.SetStateAction<string>>
  kana: string
  setKana: React.Dispatch<React.SetStateAction<string>>
  nickName: string
  setNickName: React.Dispatch<React.SetStateAction<string>>
  email: string
  setEmail: React.Dispatch<React.SetStateAction<string>>
  city: string
  setCity: React.Dispatch<React.SetStateAction<string>>
  maxAge: string
  setMaxAge: React.Dispatch<React.SetStateAction<string>>
  minAge: string
  setMinAge: React.Dispatch<React.SetStateAction<string>>
  gender: string
  setGender: React.Dispatch<React.SetStateAction<string>>
  remark: string
  setRemark: React.Dispatch<React.SetStateAction<string>>
}

const FilterInputs = ({
  fullName,
  setFullName,
  kana,
  setKana,
  nickName,
  setNickName,
  email,
  setEmail,
  city,
  setCity,
  maxAge,
  setMaxAge,
  minAge,
  setMinAge,
  gender,
  setGender,
  remark,
  setRemark,
}: FilterInputsProps) => {
  const handleTextInputChange =
    (setter: React.Dispatch<React.SetStateAction<string>>) =>
    (event: React.ChangeEvent<HTMLInputElement>) => {
      setter(event.target.value)
    }
  const handleSelectChange =
    (setter: React.Dispatch<React.SetStateAction<string>>) =>
    (event: SelectChangeEvent<string>) => {
      setter(event.target.value as string)
    }
  const handleReset = () => {
    setFullName('')
    setKana('')
    setNickName('')
    setEmail('')
    setCity('')
    setMaxAge('')
    setMinAge('')
    setGender('')
    setRemark('')
  }

  return (
    <div>
      <Stack
        direction="row"
        spacing={2}
        sx={{ justifyContent: 'space-between' }}
      >
        <TextField
          label="氏名"
          variant="outlined"
          value={fullName}
          onChange={handleTextInputChange(setFullName)}
          sx={{ backgroundColor: '#ffffff' }}
        />
        <TextField
          label="カナ名"
          variant="outlined"
          value={kana}
          onChange={handleTextInputChange(setKana)}
          sx={{ backgroundColor: '#ffffff' }}
        />
        <TextField
          label="ニックネーム"
          variant="outlined"
          value={nickName}
          onChange={handleTextInputChange(setNickName)}
          sx={{ backgroundColor: '#ffffff' }}
        />
        <TextField
          label="メールアドレス"
          variant="outlined"
          value={email}
          onChange={handleTextInputChange(setEmail)}
          sx={{ backgroundColor: '#ffffff' }}
        />
        <TextField
          label="居住地域"
          variant="outlined"
          value={city}
          onChange={handleTextInputChange(setCity)}
          sx={{ backgroundColor: '#ffffff' }}
        />
      </Stack>
      <Stack direction="row" spacing={5} sx={{ mt: 2 }}>
        <TextField
          label="最大年齢"
          variant="outlined"
          value={maxAge}
          onChange={handleTextInputChange(setMaxAge)}
          sx={{ backgroundColor: '#ffffff' }}
        />
        <TextField
          label="最小年齢"
          variant="outlined"
          value={minAge}
          onChange={handleTextInputChange(setMinAge)}
          sx={{ backgroundColor: '#ffffff' }}
        />
        <FormControl sx={{ minWidth: 120 }}>
          <InputLabel id="gender">性別</InputLabel>
          <Select
            labelId="gender"
            value={gender}
            label="gender"
            onChange={handleSelectChange(setGender)}
            sx={{ backgroundColor: '#ffffff' }}
          >
            <MenuItem value={'Male'}>Male</MenuItem>
            <MenuItem value={'Female'}>Female</MenuItem>
            <MenuItem value={'NON_BINARY'}>NON_BINARY</MenuItem>
          </Select>
        </FormControl>
        <TextField
          label="備考"
          variant="outlined"
          value={remark}
          onChange={handleTextInputChange(setRemark)}
          sx={{ backgroundColor: '#ffffff' }}
        />
        <Button
          variant="contained"
          onClick={handleReset}
          sx={{ fontWeight: 'bold', color: 'white' }}
          color="secondary"
        >
          リセット
        </Button>
      </Stack>
    </div>
  )
}

export default FilterInputs
