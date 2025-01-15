import { PagedResponse } from '@/types/api'

export const EmptyPagedResponse: PagedResponse<any> = {
  content: [],
  pageable: {
    pageNumber: 0,
    pageSize: 0,
    sort: {
      empty: false,
      sorted: false,
      unsorted: false
    },
    offset: 0,
    paged: false,
    unpaged: false
  },
  last: false,
  totalElements: 0,
  totalPages: 0,
  size: 0,
  number: 0,
  sort: {
    empty: false,
    sorted: false,
    unsorted: false
  },
  first: false,
  numberOfElements: 0,
  empty: true
}
