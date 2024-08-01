import argparse
import random
import string
import json

def generate_random_string(length=10):
    # 可以选择的字符集，这里包括大小写字母和数字
    characters = string.ascii_letters + string.digits
    # 使用random.choices从字符集中随机选择length个字符，并连接成字符串
    random_string = ''.join(random.choices(characters, k=length))
    return random_string


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--text', type=str, required=True)
    args = parser.parse_args()
    random.seed(args.text)
    # 随机生成 0-1 的数字
    score = random.random()
    # 随机生成 0-5 的整数
    category_length = random.randint(0, 5)
    # 随机生成 5-10 位的字符串
    message_length = random.randint(5, 10)

    data = {
        "category": [generate_random_string(5) for _ in range(category_length)],
        "score": f"{score}",
        "message": generate_random_string(message_length),
    }
    print(json.dumps(data))


if __name__ == '__main__':
    main()
