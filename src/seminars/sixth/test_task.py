import pytest

from task1 import NumbersList


@pytest.fixture
def list1():
    """Код в фикстуре может делать все, что вам необходимо. Вы можете использовать Fixtures, чтобы получить набор данных для тестирования.
    Вы можете использовать Fixtures, чтобы получить систему в известном состоянии перед запуском теста.
    Fixtures также используются для получения данных для нескольких тестов"""
    return [1, 2, 3, 4, 5]

@pytest.fixture
def list2():
    return [10, 10]


def test_get_lists_averages(list1, list2):
    """Проверка средних значений списков """
    nums_list = NumbersList(list1, list2)
    assert nums_list.get_list() == (3, 10)


@pytest.mark.parametrize("lst1, lst2, result", [
    ([], [1, 2, 3], (0, 2)),
    ([1, 2, 3], [], (2, 0)),
    ([], [], (0, 0))])
def test_for_empty_lists(lst1, lst2, result):
    """ тест на наличие пустых списков"""
    numbers_list = NumbersList(lst1, lst2)
    assert numbers_list.get_list() == result


@pytest.mark.parametrize("lst1, lst2, result", [
    ([1], [1, 2, 3], (1, 2)),
    ([1, 2, 3], [1], (2, 1)),
    ([1], [1], (1, 1))])
def test_for_empty_lists(lst1, lst2, result):
    """ тест на наличие в списке только одного элемента"""
    numbers_list = NumbersList(lst1, lst2)
    assert numbers_list.get_list() == result


def test_checking_average_value_when_greater(list1, list2, capfd):
    """Проверка сообщения когда среднее значение первого списка больше второго
    Capfd — фикстура для работы с потоками стандартного ввода и вывода ошибок на уровне операционной системы.
    Она позволяет перехватывать не только то, что происходит в python-коде, но и то, что происходит в операционной системе.
    Захваченный вывод доступен через вызов метода capfd.readouterr(), который возвращает named tuple с stderr и stdout в виде строк."""
    numbers_list = NumbersList(list2, list1)
    numbers_list.list_comparison()
    captured = capfd.readouterr()
    assert captured.out.strip() == 'первый список имеет среднее значение больше'


def test_checking_average_value_when_greater(list1, list2, capfd):
    """Проверка сообщения когда среднее значение второго списка больше первого"""
    numbers_list = NumbersList(list1, list2)
    numbers_list.list_comparison()
    captured = capfd.readouterr()
    assert captured.out.strip() == 'второй  список имеет среднее значение больше'


def test_checking_average_value_when_greater(list1, list2, capfd):
    """Проверка то что списки равны"""
    numbers_list = NumbersList(list1, list1)
    numbers_list.list_comparison()
    captured = capfd.readouterr()
    assert captured.out.strip() == "Списки имеют равное среднее значение"


def test_init(list1, list2):
    """Проверка корректной инициализации класса"""
    nums_list = NumbersList(list1, list2)
    assert nums_list.lst1 == list1
    assert nums_list.lst2 == list2