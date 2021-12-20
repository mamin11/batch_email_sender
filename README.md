# batch_email_sender
Spring batch application that sends email to customers whose orders have been processed ie where status = 1

## Job
- Reader 
  - read all records from orders table where status = 1
  - chunk size 100
- processor
  - send confirmation email
- writer
  - update email_sent column

## API
- launch job from API

## Database
Dummy database can be found in `test_database` directory

| order_id   |      first_name      |  last_name | email | ... | status |
|----------  |:-------------:       | ------:    | -----:| ----| ---    |
| 1          |  Doro | Odda | dodda0@jugem.jp | ... | 1 | 
