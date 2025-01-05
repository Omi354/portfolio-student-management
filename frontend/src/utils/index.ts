import axios, { AxiosResponse, AxiosError } from 'axios'

export const fetcher = (url: string) =>
  axios
    .get(url)
    .then((res: AxiosResponse) => res.data)
    .catch((err: AxiosError) => {
      console.log(err.message)
      throw err
    })

export const validationRules = {
  student: {
    fullName: {
      required: '氏名は必須です',
    },
    kana: {
      pattern: {
        // eslint-disable-next-line no-irregular-whitespace
        value: /^$|^[ァ-ヶー\s　]+$/,
        message: 'カナ名はカタカナとスペースのみを入力してください',
      },
    },
    email: {
      required: 'メールアドレスは必須です',
      pattern: {
        value: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
        message: '正しい形式のメールアドレスを入力してください。',
      },
    },
    city: {
      required: '居住地域は必須です',
    },
    age: {
      min: {
        value: 0,
        message: '年齢は0歳以上である必要があります',
      },
      max: {
        value: 150,
        message: '年齢は150歳以下である必要があります',
      },
    },
    gender: {
      validate: (value: string) => {
        return (
          ['Male', 'Female', 'NON_BINARY', ''].includes(value) ||
          '性別はMale, Female, NON_BINARY のいずれかを指定してください'
        )
      },
    },
  },
  studentCourseList: {
    courseName: {
      required: 'コース名は必須です',
    },
    startData: {
      required: '受講開始日は必須です',
    },
    endData: {
      required: '受講修了予定日は必須です',
    },
    enrollmentStatus: {
      status: {
        required: '申込状況は必須です',
        validate: (value: string) => {
          return (
            ['仮申込', '本申込', '受講中', '受講終了'].includes(value) ||
            '申込状況は仮申込, 本申込, 受講中, 受講終了 のいずれかを指定してください'
          )
        },
      },
    },
  },
}
