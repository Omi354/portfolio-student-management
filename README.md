## サービス概要

このプロジェクトは、IT技術を教えるスクールが受講生の情報を保持・分析するための管理システムです。  
受講生ではなくスクール側が使用することを想定し、CRUD機能をもつがログイン機能は持たないシンプルで使いやすいことを目指した実装内容となっています。

## 作成背景

JavaやSpring Bootの学習成果を形にするために作成しました。  
実務で頻繁に使用される以下の技術やツールを採用しています。

- REST APIの設計と実装: データのCRUD操作をサポート
- 自動テスト: JUnitを使用して単体テストを実装
- AWSを使用したデプロイ: クラウド環境へのアプリケーション展開

## 主な使用技術

### バックエンド

![badge](https://img.shields.io/badge/language-Java_21-%23007396)
![badge](https://img.shields.io/badge/SpringBoot-3.3.5-%236DB33F?logo=spring)

### インフラ・DB

![badge](https://img.shields.io/badge/AWS-EC2-FF9900?logo=amazonec2&labelColor=cccccc)
![badge](https://img.shields.io/badge/AWS-RDS-527FFF?logo=amazonrds&labelColor=cccccc)
![badge](https://img.shields.io/badge/AWS-ALB-8C4FFF?logo=awselasticloadbalancing&labelColor=cccccc)
![badge](https://img.shields.io/badge/MySQL-%234479A1?logo=mysql&logoColor=white)

### 使用ツール

![badge](https://img.shields.io/badge/MyBatis-%23DC382D?logoColor=white)
![badge](https://img.shields.io/badge/Junit5-%2325A162?logo=junit5&logoColor=white)
![badge](https://img.shields.io/badge/Postman-%23FF6C37?logo=postman&logoColor=white)
![badge](https://img.shields.io/badge/Swagger-%2385EA2D?logo=swagger&logoColor=white)
![badge](https://img.shields.io/badge/GitHub-%23181717?logo=github&logoColor=white)
![badge](https://img.shields.io/badge/GitHub_Actions-%232088FF?logo=githubactions&logoColor=white)
![badge](https://img.shields.io/badge/-intellij%20IDEA-000.svg?logo=intellij-idea&style=flat)

## 機能一覧

| 機能       | 詳細                                                             |
|:---------|:---------------------------------------------------------------|
| 受講生詳細の登録 | 氏名や居住地域などの受講生の情報と、受講コース・申込状況をセットで登録します                         |
| 受講生の条件検索 | 氏名・居住地域などの検索条件を指定し、条件に該当する受講生詳細を取得します　                         |
| 申込状況での検索 | 申込状況を指定し、該当する受講生詳細を取得します                                       |
| 受講生のID検索 | IDを指定し、一意の受講生詳細を取得します                                          |
| 受講生詳細の更新 | IDを指定し、任意の受講生詳細を更新します<br/>※削除処理については論理削除として実装しているため、更新処理にて行います |
| 申込状況の更新  | IDを指定し、任意の申込状況を更新します。<br/>誤操作を防ぐため、状況が後ろに戻るような更新は本機能では実行できません  |

※ 言葉の定義は以下のとおりです

- 受講生： 氏名、居住地域、年齢などをもつオブジェクト
- 受講コース： コース名、開始日、終了日、申込状況などをもつオブジェクト
- 申込状況： 仮申込,本申込といった申込状況、作成日などをもつオブジェクト
- 受講生詳細： 受講生、受講コース（申込状況含む）をもつオブジェクト

## 設計書

### API仕様書

### ER図

```mermaid
erDiagram
    STUDENTS {
        varchar(36) id PK
        varchar(50) full_name
        varchar(100) kana
        varchar(50) nick_name
        varchar(254) email "UNIQUE"
        varchar(50) city
        int age
        enum gender "('Male', 'Female', 'NON_BINARY', 'Unspecified')"
        text remark
        tinyint is_deleted "0"
    }
    STUDENTS_COURSES {
        varchar(36) id PK
        varchar(36) student_id FK
        varchar(100) course_name
        timestamp start_date
        timestamp end_date
    }
    ENROLLMENT_STATUSES {
        varchar(36) id PK
        varchar(36) student_course_id FK
        enum status "('仮申込', '本申込', '受講中', '受講終了')"
        timestamp created_at "CURRENT_TIMESTAMP"
    }

    STUDENTS ||--o{ STUDENTS_COURSES: "enrolls"
    STUDENTS_COURSES ||--o{ ENROLLMENT_STATUSES: "has"

```

### URL設計

| HTTP<br/>メソッド | URL                                 | 処理内容                 | 備考                            |
|---------------|-------------------------------------|----------------------|-------------------------------|
| POST          | /students                           | 受講生詳細の作成             |                               |
| GET           | /students                           | 受講生詳細の取得             | クエリパラメータを指定した場合は<br/>条件検索をします |
| GET           | /students/{id}                      | IDに合致する<br/>受講生詳細の取得 |                               |
| PUT           | /students                           | 受講生詳細の更新             |                               |
| POST          | /students/courses/enrollment-status | 申込状況の更新              | 挙動としては追加しますが<br/>実質更新処理を行います  |

### シーケンス図

```mermaid
sequenceDiagram
    actor User
    participant API as Spring Boot API
    participant DB as Database
    Note right of User: 受講生詳細の登録フロー
    User ->>+ API: POST /students (リクエストボディ：受講生詳細)
    API ->> API: 入力データ検証
    alt 入力データが有効な場合
        API ->> API: UUIDなどのデフォルト値を設定
        API ->> DB: INSERT受講生
        API ->> DB: INSERT受講コース
        API ->> DB: INSERT申込状況
        API -->> User: 200 DBに登録した受講生詳細
    else 入力データが無効な場合
        API -->>- User: 400 エラーメッセージ
    end

    Note right of User: 受講生の条件検索フロー
    User ->>+ API: GET /students (クエリパラメータ: 受講生フィールドの項目)
    API ->> API: 入力データ検証
    alt 入力データが有効な場合
        API ->> DB: SELECT受講生
        DB -->> API: 受講生
        API -->> User: 200 受講生詳細
    else 入力データが無効な場合
        API -->>- User: 400 エラーメッセージ
    end

    Note right of User: 申込状況での検索フロー
    User ->>+ API: GET /students (クエリパラメータ: 申込状況)
    API ->> API: 入力データ検証
    alt 入力データが有効な場合
        API ->> DB: SELECT受講コース（条件：申込状況）
        DB -->> API: 受講コース
        API ->> DB: SELECT受講生（条件：上記の受講コースに紐づく）
        DB -->> API: 受講生
        API -->> User: 200 受講生詳細
    else 入力データが無効な場合
        API -->>- User: 400 エラーメッセージ
    end

    Note right of User: 受講生のID検索フロー
    User ->>+ API: GET /students/id
    API ->> API: IDの形式を検証
    alt IDの形式が正しい場合
        API ->> DB: SELECT受講生
        alt IDが存在する場合
            DB -->> API: 受講生
            API ->> DB: SELECT受講生コース
            DB -->> API: 受講生コース
            API -->> User: 200 受講生詳細
        else IDが存在しない場合
            DB -->> API: データなし
            API -->> User: 404 エラーメッセージ
        end
    else IDの形式が無効な場合
        API -->>- User: 400 エラーメッセージ
    end

    Note right of User: 受講生詳細の更新フロー
    User ->>+ API: PUT /students（リクエストボディ：受講生詳細）
    API ->> API: 入力データ検証
    alt 入力データが正しい場合
        API ->> API: 入力データのIDを抽出
        API ->> DB: SELECT受講生(ID検索)
        API ->> DB: SELECT受講コース(ID検索)
        alt IDが存在する場合
            DB -->> API: データあり
            API ->> DB: UPDATE受講生
            API ->> DB: UPDATE受講コース
            API -->> User: 200 更新に成功しました
        else IDが存在しない場合
            DB -->> API: データなし
            API -->> User: 404 エラーメッセージ
        end
    else 入力データが無効な場合
        API -->>- User: 400 エラーメッセージ
    end

    Note right of User: 申込状況の更新フロー
    User ->>+ API: POST /students/courses/enrollment-status（リクエストボディ：申込状況）
    API ->> DB: SELECT申込状況（全件）
    DB -->> API: 申込状況（全件）
    API ->> API: リクエストボディの検証
    alt IDが存在する場合
        alt 申込状況が先に進むものの場合
            API ->> API: UUIDなどの初期値を設定
            API ->> DB: INSERT申込状況
            API -->> User: 200 更新に成功しました
        else 申込状況が後に戻るものの場合
            API -->> User: 400 エラーメッセージ
        end
    else IDが存在しない場合
        API -->> User: 404 エラーメッセージ
    end
```

### インフラ構成図

![](images/infrastructure-diagram.drawio.svg)

## 自動テスト

下記テストをJUnit5で作成し、GitHub Actionsでプルリクエスト作成時に実行しています。


<details>
<summary>単体テスト</summary>

- [Controller層の単体テスト](src/test/java/portfolio/StudentManagement/controller/StudentControllerTest.java)
- [Service層の単体テスト](src/test/java/portfolio/StudentManagement/service/StudentServiceTest.java)
- [Converterの単体テスト](src/test/java/portfolio/StudentManagement/controller/converter/StudentConverterTest.java)

</details>

<details>
<summary>DBテスト</summary>

- [StudentモデルのDBテスト](src/test/java/portfolio/StudentManagement/controller/converter/StudentConverterTest.java)
- [StudentCourseモデルのDBテスト](src/test/java/portfolio/StudentManagement/repository/StudentCourseRepositoryTest.java)
- [EnrollmentStatusモデルのDBテスト](src/test/java/portfolio/StudentManagement/repository/EnrollmentStatusRepositoryTest.java)

</details>

## 力をいれたところ

- **ユースケースに基づいた設計**<br>
  分析に活用することを想定し、既存のレコードを保持するようなDB処理を行っています。<br>
  具体的には、「受講生の削除機能をUPDATE処理を使い論理削除として実装」、「申込状況の更新機能をINSERT処理として実装」といった対応をしています。これにより、以下のようなデータ活用が可能です。
    - 受講生の論理削除
        - 退会者属性の傾向を分析し、マーケティングに役立てる
        - 退会者数をKPIとしてモニタリングし、一定水準を下回った場合に早期に着手できるようにする
    - 申込状況のINSERT処理
        - 仮申込状況・本申込状況の期間をカスタマーサクセスチームのKPIとして設定する
        - 受講期間をモニタリングし、講座難易度の調整に役立てる


- **効果的なバリデーション、例外処理**<br>
  正規表現などを用いてバリデーションを設定しました。<br>
  また、エラーがあった際にユーザーが適切に修正できるよう、 バリデーションエラー、ID検索の際のNot
  Foundエラー、意図していない操作をされた際のBadRequestエラーなどのハンドリングを行い、クライエント側にエラーメッセージが表示されるようにしました。


- **実装意図が伝わりやすいコーディング・ドキュメント作成**<br>
  具体的には以下の3点を行いました
    - コード内でのドキュメント作成：主要なクラス・メソッドにJavadocやOpenAPIアノテーションを利用したドキュメントを記述しました
    - 命名へのこだわり: 命名から挙動を想定できるよう、クラス名・メソッド名を工夫しました
    - 読みやすいレビュー依頼: プルリクエストでのレビュー依頼時に概要を把握しやすいよう、変更点・変更目的・特にレビューいただきたい箇所などを明示しました

## 今後の展望

- フロントエンドの実装
- インフラ構成の修正：SSMとS3を使用し、EC2のIPアドレスが変わっても動くように修正
- アカウント作成・ログイン機能の実装
- テーブルを跨いだ検索機能の実装
- 結合テストの自動テストを作成