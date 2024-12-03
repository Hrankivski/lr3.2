В обох методах, Work Stealing та Work Dealing, програма ефективно обходить директорії та рахує кількість файлів, які містять задане користувачем слово. Проте, часи виконання цих методів відрізняються, що підкреслює значення правильного вибору стратегії розподілу задач відповідно до конкретної задачі та обчислювального навантаження.

Порівняння підходів

Work Stealing. 
У цьому підході потоки активно "викрадають" завдання з загальної черги, коли їхні власні завдання завершено. Ця стратегія може бути особливо ефективною в умовах, де навантаження на потоки не є рівномірно розподілене, дозволяючи динамічно балансувати робоче навантаження по мірі виконання задач.
Часи виконання: На практиці, часи виконання для Work Stealing можуть бути довшими, що виявлено в результатах, особливо в складних директоріях з великою кількістю файлів.

Work Dealing. 
Завдання розподіляються між потоками на самому початку, і кожен потік обробляє свій призначений набір завдань. Це може мінімізувати накладні витрати, пов’язані з управлінням чергами завдань та перевіркою доступності нових завдань.
Часи виконання: Результати показують, що Work Dealing забезпечує кращий час виконання в порівнянні з Work Stealing, особливо у сценаріях із значним обсягом даних.

Висновки та рекомендації

На основі зібраних даних та аналізу, можна зробити висновок, що метод Work Dealing є більш ефективним для завдань пошуку файлів у великих директоріях. Це пов’язано з тим, що призначення завдань на початку дозволяє краще розподілити робоче навантаження і уникнути накладних витрат, що виникають при динамічному балансуванні.
Рекомендується використовувати підхід Work Dealing для аналогічних завдань у сфері обробки файлів, особливо коли структура даних добре відома і може бути ефективно розподілена на початку. Work Stealing може бути застосований в ситуаціях, де навантаження непередбачуване або може змінюватися в процесі виконання, забезпечуючи гнучкість в розподілі ресурсів.
