import {
  Bedroom1,
  Bathroom1,
  Attic1,
  LivingRoom1,
  Kitchen1,
} from '../assets/images';

export const getRoomImage = (roomType) => {
    switch (roomType) {
      case 'Bedroom':
        return Bedroom1;
      case 'Bathroom':
        return Bathroom1;
      case 'Attic':
        return Attic1;
      case 'Living_Room':
        return LivingRoom1;
      case 'Kitchen':
        return Kitchen1;
      default:
        break;
    }
  };
