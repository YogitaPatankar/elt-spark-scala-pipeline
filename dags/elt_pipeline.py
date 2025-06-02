from airflow import DAG
from airflow.operators.bash import BashOperator
from datetime import datetime, timedelta

default_args = {
    'owner': 'airflow',
    'start_date': datetime(2023, 1, 1),
    'retries': 1,
    'retry_delay': timedelta(minutes=5),
}

with DAG('elt_spark_pipeline', default_args=default_args, schedule_interval='@hourly', catchup=False) as dag:
    run_spark_job = BashOperator(task_id='run_spark_etl', bash_command='docker-compose run --rm spark-job')
    run_spark_job