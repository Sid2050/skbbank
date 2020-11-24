select fio, year(date_receive) as year, month(date_receive) as month, avg(round(estimation, 2)) as average from public.classes
where (:numb::int) = class_number
group by fio, year(date_receive), month(date_receive)
order by fio asc